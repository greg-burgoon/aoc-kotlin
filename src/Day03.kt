
import kotlin.collections.HashMap

fun main() {

    var priorityValues = HashMap<Char, Int>()

    var count = 1
    for(c in 'a'..'z') {
        priorityValues.put(c, count)
        count++
    }

    for(c in 'A'..'Z') {
        priorityValues.put(c, count)
        count++
    }

    fun part1(input: String): Int {
        return input.split("\n")
            .map {
                listOf(it.substring(0, it.length/2).toSet(), it.substring(it.length/2).toSet())
            }.map {(setOne, setTwo) ->
                priorityValues.getOrDefault(setOne.intersect(setTwo)
                    .first(), 0)
            }
            .sum()
    }

    fun part2(input: String): Int {
        return input.split("\n")
            .windowed(3, 3)
            .flatMap { (first, second, third) ->
                first.toSet()
                    .intersect(second.toSet())
                    .intersect(third.toSet())
                    .map {
                        c: Char -> priorityValues.getOrDefault(c, 0)
                    }
            }.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val output = part1(testInput)
    check(output== 157)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
