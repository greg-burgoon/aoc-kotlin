import kotlin.math.sqrt

enum class DIRECTION {
    UP, DOWN, LEFT, RIGHT
}

fun main() {
    class Position(x: Int, y: Int, tail: Position?){
        var x = x
        var y = y
        val tail = tail
        var positionHistory = HashSet<Position>()

        fun copyPosition(): Position{
            return Position(this.x, this.y, null)
        }

        fun adjustTail() {
            if (tail == null) {
                return
            }
            val deltaX = this.x - tail.x
            val deltaY = this.y - tail.y
            val distance = sqrt(((deltaX*deltaX) + (deltaY*deltaY)).toDouble())
            if (distance == 2.0) {
                //decide if we go with delta X, delta Y
                if (deltaX == 0) {
                    if (Math.abs(deltaY) == deltaY) {
                        tail.y++
                    } else {
                        tail.y--
                    }
                } else {
                    if (Math.abs(deltaX) == deltaX) {
                        tail.x++
                    } else {
                        tail.x--
                    }
                }

            } else if (distance > 2.0) {
                if (Math.abs(deltaX) == deltaX) {
                    tail.x++
                } else {
                    tail.x--
                }
                if (Math.abs(deltaY) == deltaY) {
                    tail.y++
                } else {
                    tail.y--
                }
            }
            tail.adjustTailAndHistory()
        }

        fun adjustTailAndHistory() {
            this.positionHistory.add(this.copyPosition())
            this.adjustTail()
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
        var tail = Position(0,0, null)
        var head = Position(0,0, tail)
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
                            head.adjustTail()
                        }
                    }
                    DIRECTION.DOWN -> {
                        repeat(steps) {
                            head.y--
                            head.adjustTail()
                        }
                    }
                    DIRECTION.LEFT -> {
                        repeat(steps) {
                            head.x--
                            head.adjustTail()
                        }
                    }
                    DIRECTION.RIGHT -> {
                        repeat(steps) {
                            head.x++
                            head.adjustTail()
                        }
                    }
                }
            }
        return tail.positionHistory.size
    }

    fun part2(input: String): Int {
        var tail = Position(0,0, null)
        var previousTail = tail
        var head = previousTail
        repeat(9) {
            head = Position(0,0, previousTail)
            previousTail = head
        }

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
                            head.adjustTail()
                        }
                    }
                    DIRECTION.DOWN -> {
                        repeat(steps) {
                            head.y--
                            head.adjustTail()
                        }
                    }
                    DIRECTION.LEFT -> {
                        repeat(steps) {
                            head.x--
                            head.adjustTail()
                        }
                    }
                    DIRECTION.RIGHT -> {
                        repeat(steps) {
                            head.x++
                            head.adjustTail()
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

    val testInputTwo = readInput("Day09_test2")
    val outputTwo = part2(testInputTwo)
    check(outputTwo == 36)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))

}
