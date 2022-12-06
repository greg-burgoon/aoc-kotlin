import java.util.*

fun main() {
    fun part1(input: String): Int {
        return input.split("\n\n")
            .map {
                it.split("\n").sumOf {
                    it.toInt()
                }
            }.max()
    }

    fun part2(input: String): Int {
        return input.split("\n\n")
            .map {
                it.split("\n").sumOf {
                    it.toInt()
                }
            }.sortedDescending()
            .subList(0,3)
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
