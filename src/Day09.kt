import kotlin.math.sqrt

enum class DIRECTION {
    UP, DOWN, LEFT, RIGHT
}

fun main() {
    class Position(x: Int, y: Int){
        var x = x
        var y = y
        var positionHistory = HashSet<Position>()

        fun copyPosition(): Position{
            return Position(this.x, this.y)
        }

        fun keepUpWithPosition(headPosition: Position) {
            val deltaX = headPosition.x - this.x
            val deltaY = headPosition.y - this.y
            val distance = sqrt(((deltaX*deltaX) + (deltaY*deltaY)).toDouble())
            if (distance == 2.0) {
                //decide if we go with delta X, delta Y
                if (deltaX == 0) {
                    if (Math.abs(deltaY) == deltaY) {
                        this.y++
                    } else {
                        this.y--
                    }
                } else {
                    if (Math.abs(deltaX) == deltaX) {
                        this.x++
                    } else {
                        this.x--
                    }
                }

            } else if (distance > 2.0) {
                if (Math.abs(deltaX) == deltaX) {
                    this.x++
                } else {
                    this.x--
                }
                if (Math.abs(deltaY) == deltaY) {
                    this.y++
                } else {
                    this.y--
                }
            }
            positionHistory.add(this.copyPosition())
        }

        override fun equals(other: Any?): Boolean =
            other is Position
                    && other.x == x
                    && other.y == y

        override fun hashCode(): Int {
            return x.hashCode() + y.hashCode()
        }
    }

    fun part1(input: String): Int {
        var head = Position(0,0)
        var tail = Position(0,0)
        input.split("\n")
            .map {
                listOf(it.split(" ")[0], it.split(" ")[1])
            }
            .forEach { (d, s) ->
                val steps = s.toInt()
                val direction = if (d == "U") DIRECTION.UP else if (d == "D") DIRECTION.DOWN else if (d == "L") DIRECTION.LEFT else DIRECTION.RIGHT
                when (direction) {
                    DIRECTION.UP -> {
                        repeat(steps) {
                            head.y++
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.DOWN -> {
                        repeat(steps) {
                            head.y--
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.LEFT -> {
                        repeat(steps) {
                            head.x--
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.RIGHT -> {
                        repeat(steps) {
                            head.x++
                            tail.keepUpWithPosition(head)
                        }
                    }
                }
            }
        return tail.positionHistory.size
    }

    fun part2(input: String): Int {
        var head = Position(0,0)
        var tail = Position(0,0)
        input.split("\n")
            .map {
                listOf(it.split(" ")[0], it.split(" ")[1])
            }
            .forEach { (d, s) ->
                val steps = s.toInt()
                val direction = if (d == "U") DIRECTION.UP else if (d == "D") DIRECTION.DOWN else if (d == "L") DIRECTION.LEFT else DIRECTION.RIGHT
                when (direction) {
                    DIRECTION.UP -> {
                        repeat(steps) {
                            head.y++
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.DOWN -> {
                        repeat(steps) {
                            head.y--
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.LEFT -> {
                        repeat(steps) {
                            head.x--
                            tail.keepUpWithPosition(head)
                        }
                    }
                    DIRECTION.RIGHT -> {
                        repeat(steps) {
                            head.x++
                            tail.keepUpWithPosition(head)
                        }
                    }
                }
            }
        return tail.positionHistory.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val output = part1(testInput)
    check(output == 13)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))

}
