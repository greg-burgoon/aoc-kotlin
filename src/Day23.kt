
enum class Direction {
    NORTH, SOUTH, EAST, WEST
}
fun main() {

    data class Coord(val y: Int, val x: Int) {

        fun N(): Coord{
            return Coord(y-1, x)
        }

        fun S(): Coord{
            return Coord(y+1, x)
        }

        fun E(): Coord{
            return Coord(y, x+1)
        }

        fun W(): Coord{
            return Coord(y, x-1)
        }

        fun NW(): Coord{
            return Coord(y-1, x-1)
        }

        fun NE(): Coord{
            return Coord(y-1, x+1)
        }

        fun SW(): Coord{
            return Coord(y+1, x-1)
        }

        fun SE(): Coord{
            return Coord(y+1, x+1)
        }

        override fun toString(): String {
            return "Coord(y=$y, x=$x)"
        }


    }

    fun printGrid(coordMap: MutableMap<Coord, Boolean>) {
        var minX = coordMap.filter { it.value }.keys.minOf { it.x }
        var maxX = coordMap.filter { it.value }.keys.maxOf { it.x }
        var minY = coordMap.filter { it.value }.keys.minOf { it.y }
        var maxY = coordMap.filter { it.value }.keys.maxOf { it.y }

        for (y in minY .. maxY) {
            for (x in minX .. maxX) {
                if (coordMap.getOrDefault(Coord(y,x), false)) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println("")
        }
    }

    fun part1(input: String): Int {
        var coordMap = mutableMapOf<Coord, Boolean>()
        input.split("\n").forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    coordMap[Coord(y,x)] = true
                }
            }
        }
        var numberOfElves = coordMap.keys.size

        var moved = true
        var directionSequence = ArrayDeque<Direction>()
        directionSequence.add(Direction.NORTH)
        directionSequence.add(Direction.SOUTH)
        directionSequence.add(Direction.WEST)
        directionSequence.add(Direction.EAST)
        var round = 0
        println("Round: "+round)
        printGrid(coordMap)
        repeat (10) {
            var proposedMoves = mutableMapOf<Coord, Int>()
            var transitions = mutableMapOf<Coord, Coord>()
            coordMap.filter { it.value == true }.keys.forEach { coord ->
                var nextCoord: Coord? = null
                var moveMap = mutableMapOf<Direction,Int>()
                directionSequence.forEach {
                    when(it) {
                        Direction.NORTH -> {
                            var canMove = !coordMap.getOrDefault(coord.NW(), false) &&
                                !coordMap.getOrDefault(coord.N(), false) &&
                                !coordMap.getOrDefault(coord.NE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.N()
                            }
                            if (canMove) {
                                moveMap[Direction.NORTH] = 1
                            }
                        }
                        Direction.SOUTH -> {
                            var canMove = !coordMap.getOrDefault(coord.SW(), false) &&
                                    !coordMap.getOrDefault(coord.S(), false) &&
                                    !coordMap.getOrDefault(coord.SE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.S()
                            }
                            if (canMove) {
                                moveMap[Direction.SOUTH] = 1
                            }
                        }
                        Direction.WEST -> {
                            var canMove = !coordMap.getOrDefault(coord.NW(), false) &&
                                    !coordMap.getOrDefault(coord.W(), false) &&
                                    !coordMap.getOrDefault(coord.SW(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.W()
                            }
                            if (canMove) {
                                moveMap[Direction.WEST] = 1
                            }
                        }
                        else -> {
                            var canMove = !coordMap.getOrDefault(coord.NE(), false) &&
                                    !coordMap.getOrDefault(coord.E(), false) &&
                                    !coordMap.getOrDefault(coord.SE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.E()
                            }
                            if (canMove) {
                                moveMap[Direction.EAST] = 1
                            }
                        }
                    }
                }
                if (moveMap.values.sum() == 4) {
                    //noop
                } else if (nextCoord != null) {
//                    println("Proposing moving "+coord+" to "+nextCoord!!)
                    proposedMoves[nextCoord!!] = proposedMoves.getOrDefault(nextCoord!!, 0) + 1
                    transitions[nextCoord!!] = coord
                }
            }
            var newMoves = proposedMoves.filter {
                it.value == 1
            }

            newMoves.keys.forEach {
//                println("Moved "+transitions[it]!!+" to "+it)
                coordMap[transitions[it]!!] = false
                coordMap[it] = true
            }
            moved = newMoves.isNotEmpty()
            directionSequence.addLast(directionSequence.removeFirst())
            round++
            println("Round: "+round)
            printGrid(coordMap)
        }

        var minX = coordMap.filter { it.value }.keys.minOf { it.x }
        var maxX = coordMap.filter { it.value }.keys.maxOf { it.x }
        var minY = coordMap.filter { it.value }.keys.minOf { it.y }
        var maxY = coordMap.filter { it.value }.keys.maxOf { it.y }
        var length = maxX - minX + 1
        var width = maxY - minY + 1
        var emptyTiles = (length*width) - numberOfElves
        return emptyTiles
    }

    fun part2(input: String): Int {
        var coordMap = mutableMapOf<Coord, Boolean>()
        input.split("\n").forEachIndexed { y, s ->
            s.forEachIndexed { x, c ->
                if (c == '#') {
                    coordMap[Coord(y,x)] = true
                }
            }
        }
        var numberOfElves = coordMap.keys.size

        var moved = true
        var directionSequence = ArrayDeque<Direction>()
        directionSequence.add(Direction.NORTH)
        directionSequence.add(Direction.SOUTH)
        directionSequence.add(Direction.WEST)
        directionSequence.add(Direction.EAST)
        var round = 0
        while (moved) {
            var proposedMoves = mutableMapOf<Coord, Int>()
            var transitions = mutableMapOf<Coord, Coord>()
            coordMap.filter { it.value == true }.keys.forEach { coord ->
                var nextCoord: Coord? = null
                var moveMap = mutableMapOf<Direction,Int>()
                directionSequence.forEach {
                    when(it) {
                        Direction.NORTH -> {
                            var canMove = !coordMap.getOrDefault(coord.NW(), false) &&
                                    !coordMap.getOrDefault(coord.N(), false) &&
                                    !coordMap.getOrDefault(coord.NE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.N()
                            }
                            if (canMove) {
                                moveMap[Direction.NORTH] = 1
                            }
                        }
                        Direction.SOUTH -> {
                            var canMove = !coordMap.getOrDefault(coord.SW(), false) &&
                                    !coordMap.getOrDefault(coord.S(), false) &&
                                    !coordMap.getOrDefault(coord.SE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.S()
                            }
                            if (canMove) {
                                moveMap[Direction.SOUTH] = 1
                            }
                        }
                        Direction.WEST -> {
                            var canMove = !coordMap.getOrDefault(coord.NW(), false) &&
                                    !coordMap.getOrDefault(coord.W(), false) &&
                                    !coordMap.getOrDefault(coord.SW(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.W()
                            }
                            if (canMove) {
                                moveMap[Direction.WEST] = 1
                            }
                        }
                        else -> {
                            var canMove = !coordMap.getOrDefault(coord.NE(), false) &&
                                    !coordMap.getOrDefault(coord.E(), false) &&
                                    !coordMap.getOrDefault(coord.SE(), false)
                            if (nextCoord == null && canMove) {
                                nextCoord = coord.E()
                            }
                            if (canMove) {
                                moveMap[Direction.EAST] = 1
                            }
                        }
                    }
                }
                if (moveMap.values.sum() == 4) {
                    //noop
                } else if (nextCoord != null) {
                    proposedMoves[nextCoord!!] = proposedMoves.getOrDefault(nextCoord!!, 0) + 1
                    transitions[nextCoord!!] = coord
                }
            }
            var newMoves = proposedMoves.filter {
                it.value == 1
            }

            newMoves.keys.forEach {
                coordMap[transitions[it]!!] = false
                coordMap[it] = true
            }
            moved = newMoves.isNotEmpty()
            directionSequence.addLast(directionSequence.removeFirst())
            round++
        }
        return round
    }

    val testInput = readInput("Day23_test")
    val output = part1(testInput)
    check(output == 110)

    val outputTwo = part2(testInput)
    check(outputTwo == 20)

    val input = readInput("Day23")
    println(part1(input))
    println(part2(input))
}
