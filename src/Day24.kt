import java.util.TreeSet
import kotlin.time.Duration.Companion.minutes

enum class MapTile (var char: Char) {
    UP('^'), DOWN('v'), LEFT('<'), RIGHT('>'), NONE('.')
}
fun main() {
    data class Coord(val row: Int, val col: Int) {
        fun getSurroundingCoords(): List<Coord> {
            return listOf(Coord(row-1, col), Coord(row+1, col), Coord(row, col-1), Coord(row, col+1))
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Coord

            if (row != other.row) return false
            if (col != other.col) return false

            return true
        }

        override fun hashCode(): Int {
            var result = row
            result = 31 * result + col
            return result
        }
    }

    data class State(var position: Coord, var minutes: Long, val cycleLength: Long): Comparable<State> {
        override fun compareTo(other: State): Int {
            if (position == other.position && minutes == other.minutes) {
                return 0
            } else if (position.row + position.col - Math.abs(minutes) > other.position.row + other.position.col - Math.abs(other.minutes)) {
                return 1
            } else {
//                if (position == other.position && minutes < other.minutes) {
//                    return 1
//                }
                return -1
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as State

            if (position != other.position) return false
            if (minutes.mod(cycleLength) != (other.minutes.mod(cycleLength))) return false

            return true
        }

        override fun hashCode(): Int {
            var result = position.hashCode()
            result = (31 * result + minutes).toInt()
            return result
        }

    }

    data class Blizzard(val mapTile: MapTile, var row: Int, var col: Int) {
        fun moveBlizzard(rowNums: Int, colNums: Int) {
            when(mapTile) {
                MapTile.UP -> this.row = (this.row - 1).mod(rowNums)
                MapTile.DOWN -> this.row = (this.row + 1).mod(rowNums)
                MapTile.LEFT -> this.col = (this.col - 1).mod(colNums)
                MapTile.RIGHT -> this.col = (this.col + 1).mod(colNums)
                else -> {}//noop
            }
        }
    }

    fun lcm(numberOne: Int, numberTwo: Int): Long {
        var i = 1
        var gcd = 1
        while (i <= numberOne && i <= numberTwo) {
            // Checks if i is factor of both integers
            if (numberOne % i == 0 && numberTwo % i == 0)
                gcd = i
            ++i
        }

        return (numberOne * numberTwo / gcd).toLong()
    }

    fun updateBlizzards(blizzards: MutableList<Blizzard>, blizzardsMap: MutableMap<Coord, Boolean>, rowNums: Int, colNums: Int) {
        blizzardsMap.clear()
        blizzards.filter { it.mapTile != MapTile.NONE }.forEach {
            it.moveBlizzard(rowNums, colNums)
            blizzardsMap[Coord(it.row, it.col)] = true
        }
    }

    fun printBlizzards(blizzardMap: Map<Coord, Boolean>, map: List<List<MapTile>>) {
        map.forEachIndexed { row, mapTile ->
            mapTile.forEachIndexed { col, mapTile ->
                if (blizzardMap.getOrDefault(Coord(row, col), false)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println("")
        }
        println("")
    }

    fun part1(input: String): Long {
        var matrix = input.filter { c: Char -> c != '#' }.split("\n").drop(1).dropLast(1)

        var mapTiles = matrix.mapIndexed { row, rowString ->
            rowString.map { c: Char ->
                when(c){
                    '^' -> MapTile.UP
                    'v' -> MapTile.DOWN
                    '<' -> MapTile.LEFT
                    '>' -> MapTile.RIGHT
                    else -> MapTile.NONE
                }
            }.toMutableList()
        }.toMutableList()

        var blizzards = mapTiles.flatMapIndexed { row, mapTiles -> mapTiles.mapIndexed { col, mapTile ->  Blizzard(mapTile, row, col)} }.toMutableList()
        var blizzardMap = blizzards.map {
            Coord(it.row, it.col) to (it.mapTile != MapTile.NONE)
        }.toMap().toMutableMap()

        var mapAtTime = mutableMapOf<Int, Map<Coord, Boolean>>()
        val cycleLength = lcm(mapTiles.size, mapTiles[0].size)
        repeat(cycleLength.toInt()) {
            mapAtTime[it] = blizzardMap.toMap()
            updateBlizzards(blizzards, blizzardMap, mapTiles.size, mapTiles[0].size)
        }

        var distanceStack = TreeSet<State>()
        mapTiles.forEachIndexed { row, mapTiles ->
            mapTiles.forEachIndexed { col, mapTile ->
                distanceStack.add(State(Coord(row,col), Long.MAX_VALUE, cycleLength))
            }
        }

        var shortestPathMap = mutableMapOf<Coord, Long>()
        var startCoord = Coord(-1,0)
        distanceStack.add(State(startCoord, 0, cycleLength))
        while (shortestPathMap.size < mapTiles.flatten().size) {
            var currentState = distanceStack.pollLast()
            if (shortestPathMap.getOrDefault(currentState.position, Long.MAX_VALUE) > currentState.minutes) {
                shortestPathMap[currentState.position] = currentState.minutes
            }
            val mapKey = (currentState.minutes+1).mod(cycleLength).toInt()
            var bMapAtTime = mapAtTime.get(mapKey)!!
//            println("Current State: $currentState")
//            println("Using Map: ")
//            printBlizzards(bMapAtTime, mapTiles)
            var moveOptions = currentState.position.getSurroundingCoords().toMutableList()
            moveOptions.add(currentState.position)
            moveOptions.filter { it.row >= 0 && it.row < mapTiles.size && it.col >= 0 && it.col < mapTiles[0].size }.forEach { coord ->
                if (!bMapAtTime.getOrDefault(coord, false)) {
                    var nextState = State(coord, currentState.minutes+1, cycleLength)
                    var curPath = distanceStack.find { it == nextState }
                    if (curPath != null && nextState.minutes < curPath.minutes) {
                        distanceStack.remove(curPath)
                        distanceStack.add(nextState)
                    } else if (curPath == null) {
                        distanceStack.add(nextState)
                    }
                }
            }
            if (currentState.position == startCoord) {
                var nextState = State(currentState.position, currentState.minutes+1, cycleLength)
                distanceStack.add(nextState)
            }
        }
        var lastSpaceMinutes = shortestPathMap.get(Coord(mapTiles.size-1, mapTiles[0].size-1))!!
        return lastSpaceMinutes + 1
    }

    fun part2(input: String): Int {
        return 0
    }

    val testInput = readInput("Day24_test")
    val output = part1(testInput)
    check(output == 18L)

//    val outputTwo = part2(testInput)
//    check(outputTwo == 20)

    val input = readInput("Day24")
    println(part1(input))
    println(part2(input))
}
