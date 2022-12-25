import java.lang.Exception

enum class Movement(val points: Int) {
    UP(3), DOWN(1), LEFT(2), RIGHT(0)
}

interface Transformer {
    fun transformUp(currentPosition: List<Int>): MutableList<Int>
    fun transformDown(currentPosition: List<Int>): MutableList<Int>
    fun transformLeft(currentPosition: List<Int>): MutableList<Int>
    fun transformRight(currentPosition: List<Int>): MutableList<Int>
}

fun main() {

    class WrapTransformer() : Transformer {
        val rowRanges: MutableList<MutableList<IntRange>> = mutableListOf<MutableList<IntRange>>()
        val colRanges: MutableList<MutableList<IntRange>> = mutableListOf<MutableList<IntRange>>()

        fun findRowRangeForPosition(position: List<Int>): IntRange {
            rowRanges[position[0]].forEach {
                if (position[1] in it) return it
            }
            throw Exception()
        }

        fun findColRangeForPosition(position: List<Int>): IntRange {
            colRanges[position[1]].forEach {
                if (position[0] in it) return it
            }
            throw NullPointerException()
        }
        override fun transformUp(currentPosition: List<Int>): MutableList<Int> {
            var colRange = findColRangeForPosition(currentPosition)
            return mutableListOf(colRange.last, currentPosition[1])
        }

        override fun transformDown(currentPosition: List<Int>): MutableList<Int> {
            var colRange = findColRangeForPosition(currentPosition)
            return mutableListOf(colRange.first, currentPosition[1])
        }

        override fun transformLeft(currentPosition: List<Int>): MutableList<Int> {
            var rowRange = findRowRangeForPosition(currentPosition)
            return mutableListOf(currentPosition[0], rowRange.last)
        }

        override fun transformRight(currentPosition: List<Int>): MutableList<Int> {
            var rowRange = findRowRangeForPosition(currentPosition)
            return mutableListOf(currentPosition[0], rowRange.first)
        }
    }

    class CubeTransformer() : Transformer {
        override fun transformUp(currentPosition: List<Int>): MutableList<Int> {
            TODO("Not yet implemented")
        }

        override fun transformDown(currentPosition: List<Int>): MutableList<Int> {
            TODO("Not yet implemented")
        }

        override fun transformLeft(currentPosition: List<Int>): MutableList<Int> {
            TODO("Not yet implemented")
        }

        override fun transformRight(currentPosition: List<Int>): MutableList<Int> {
            TODO("Not yet implemented")
        }
    }

    data class OverviewMap(val transformer: Transformer,
                            val grid: MutableList<MutableList<Char>> = mutableListOf<MutableList<Char>>(),
                            var starterTile: MutableList<Int> = mutableListOf<Int>(-1,-1)
    )

    data class InstructionProcessor(val instructions: String,
                                    val leftTransform: (currentDir: Movement) -> Movement,
                                    val rightTransform: (currentDir: Movement) -> Movement,
                                    val map: OverviewMap,
        ) {
        var currentDirection = Movement.RIGHT
        var currentPosition = map.starterTile

        fun processInstructions(): List<Int>{
            instructions
                .replace("L", "|L|")
                .replace("R","|R|")
                .split("|").forEach {
                    if (it.isNotEmpty()) {
                        if (it.toIntOrNull() != null) {
                            var steps = it.toInt()
                            repeat(steps) {
                                when(currentDirection){
                                    Movement.UP -> {
                                        processMoveUp()
                                    }
                                    Movement.DOWN -> {
                                        processMoveDown()
                                    }
                                    Movement.LEFT -> {
                                        processMoveLeft()
                                    }
                                    else -> {
                                        processMoveRight()
                                    }
                                }
                                println("POS: row:"+ currentPosition[0]+", col:"+currentPosition[1])
                            }
                        } else {
                            if (it == "L") {
                                currentDirection = leftTransform(currentDirection)
                            } else {
                                currentDirection = rightTransform(currentDirection)
                            }
                        }
                    }
                }
            return currentPosition
        }

        private fun processMoveRight(): Boolean {
            var nextPosition = mutableListOf(currentPosition[0], currentPosition[1] + 1)
            if (map.grid[nextPosition[0]][nextPosition[1]] == ' ') {
                nextPosition = map.transformer.transformRight(currentPosition)
            }
            if (map.grid[nextPosition[0]][nextPosition[1]] != '#') {
                currentPosition = nextPosition
                return true
            }
            return false
        }

        private fun processMoveLeft(): Boolean {
            var nextPosition = mutableListOf(currentPosition[0], currentPosition[1] - 1)
            if (map.grid[nextPosition[0]][nextPosition[1]] == ' ') {
                nextPosition = map.transformer.transformLeft(currentPosition)
            }
            if (map.grid[nextPosition[0]][nextPosition[1]] != '#') {
                currentPosition = nextPosition
                return true
            }
            return false
        }

        private fun processMoveDown(): Boolean {
            var nextPosition = mutableListOf(currentPosition[0]+1, currentPosition[1])
            if (map.grid[nextPosition[0]][nextPosition[1]] == ' ') {
                nextPosition = map.transformer.transformDown(currentPosition)
            }
            if (map.grid[nextPosition[0]][nextPosition[1]] != '#') {
                currentPosition = nextPosition
                return true
            }
            return false
        }

        private fun processMoveUp(): Boolean {
            var nextPosition = mutableListOf(currentPosition[0]-1, currentPosition[1])
            if (map.grid[nextPosition[0]][nextPosition[1]] == ' ') {
                nextPosition = map.transformer.transformUp(currentPosition)
            }
            if (map.grid[nextPosition[0]][nextPosition[1]] != '#') {
                currentPosition = nextPosition
                return true
            }
            return false
        }
    }

    fun padMapWithSpaces(mapWidth: Int, mapString: String): String {
        var paddedMapString = ""
        repeat(mapWidth) {
            paddedMapString += ' '
        }
        paddedMapString += "\n"
        paddedMapString += mapString + "\n"
        repeat(mapWidth) {
            paddedMapString += ' '
        }
        return paddedMapString
    }

    fun createMapForWrapTransformer(mapString: String, transformer: WrapTransformer): OverviewMap {
        var map = OverviewMap(transformer)
        var mapWidth = mapString.split("\n").maxOf { it.length }
        var paddedMapString = padMapWithSpaces(mapWidth, mapString)
        paddedMapString.split("\n").forEachIndexed { row, s ->
            map.grid.add(mutableListOf<Char>())
            var rowString = ' ' + s
            repeat((mapWidth-s.length)+1){
                rowString += ' '
            }
            transformer.rowRanges.add(mutableListOf<IntRange>())
            var previousVal = '#'
            var previousRangeIndex = 0
            rowString.forEachIndexed { col, c ->
                map.grid[row].add(c)
                if (map.starterTile[0] == -1 && map.starterTile[1] == -1 &&
                    (c != ' ')) {
                    map.starterTile[0] = row
                    map.starterTile[1] = col
                }
                if ((previousVal == ' ' && c == ' ') || (previousVal != ' ' && c != ' ')) {
                    //noop
                } else if (col != 0) {
                    transformer.rowRanges[row].add(previousRangeIndex until col)
                    previousRangeIndex = col
                }
                previousVal = c
            }
            transformer.rowRanges[row].add(previousRangeIndex until rowString.length)
        }

        var previousColumnVal = mutableListOf<Char>()
        repeat(map.grid[0].size){
            previousColumnVal.add(' ')
        }
        var previousColumnRangeIndex = mutableListOf<Int>()
        repeat(map.grid[0].size){
            previousColumnRangeIndex.add(0)
        }

        repeat(mapWidth+2) {
            transformer.colRanges.add(mutableListOf<IntRange>())
        }
        map.grid.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, c ->
                if ((previousColumnVal[col] == ' ' && c == ' ') || (previousColumnVal[col] != ' ' && c != ' ')) {
                    //noop
                } else if (row != 0) {
                    transformer.colRanges[col].add(previousColumnRangeIndex[col] until row)
                    previousColumnRangeIndex[col] = row
                }
                previousColumnVal[col] = c
            }
        }
        previousColumnRangeIndex.forEachIndexed { col, i ->
            transformer.colRanges[col].add(previousColumnRangeIndex[col] until map.grid.size)
        }
        return map
    }

    fun createMapForCubeTransformer(mapString: String, transformer: CubeTransformer): OverviewMap {
        var map = OverviewMap(transformer)
        var mapWidth = mapString.split("\n").maxOf { it.length }
        var paddedMapString = padMapWithSpaces(mapWidth, mapString)
        paddedMapString.split("\n").forEachIndexed { row, s ->
            map.grid.add(mutableListOf<Char>())
            var rowString = ' ' + s
            repeat((mapWidth-s.length)+1){
                rowString += ' '
            }
            rowString.forEachIndexed { col, c ->
                map.grid[row].add(c)
                if (map.starterTile[0] == -1 && map.starterTile[1] == -1 &&
                    (c != ' ')) {
                    map.starterTile[0] = row
                    map.starterTile[1] = col
                }
            }
        }
        return map
    }

    fun splitMapAndInstructions(input: String): List<String> {
        val inputs = input.split("\n\n")
        return listOf(inputs[0],inputs[1])
    }

    fun part1(input: String): Int {
        var data = splitMapAndInstructions(input)
        val instructionProcessor = InstructionProcessor(
            data[1],
            { currentDirection: Movement ->
                when(currentDirection) {
                    Movement.UP -> Movement.LEFT
                    Movement.DOWN -> Movement.RIGHT
                    Movement.LEFT -> Movement.DOWN
                    else -> { Movement.UP }
                }
            },
            { currentDirection: Movement ->
                when(currentDirection) {
                    Movement.UP -> Movement.RIGHT
                    Movement.DOWN -> Movement.LEFT
                    Movement.LEFT -> Movement.UP
                    else -> { Movement.DOWN }
                }
            },
            createMapForWrapTransformer(data[0], WrapTransformer())
        )

        instructionProcessor.processInstructions()
        return (1000*instructionProcessor.currentPosition[0]) + (4*instructionProcessor.currentPosition[1]) + (instructionProcessor.currentDirection.points)
    }

    fun part2(input: String): Int {
        var cubeTransformer = CubeTransformer()
        var data = splitMapAndInstructions(input)
        val instructionProcessor = InstructionProcessor(
            data[1],
            { currentDirection: Movement ->
                when(currentDirection) {
                    Movement.UP -> Movement.LEFT
                    Movement.DOWN -> Movement.RIGHT
                    Movement.LEFT -> Movement.DOWN
                    else -> { Movement.UP }
                }
            },
            { currentDirection: Movement ->
                when(currentDirection) {
                    Movement.UP -> Movement.RIGHT
                    Movement.DOWN -> Movement.LEFT
                    Movement.LEFT -> Movement.UP
                    else -> { Movement.DOWN }
                }
            },
            createMapForCubeTransformer(data[0], cubeTransformer)
        )

        instructionProcessor.processInstructions()
        return (1000*instructionProcessor.currentPosition[0]) + (4*instructionProcessor.currentPosition[1]) + (instructionProcessor.currentDirection.points)
    }

    val testInput = readInput("Day22_test")
    val output = part1(testInput)
    check(output == 6032)

    val outputTwo = part2(testInput)
    check(outputTwo == 5031)

    val input = readInput("Day22")
    println(part1(input))
    println(part2(input))
}
