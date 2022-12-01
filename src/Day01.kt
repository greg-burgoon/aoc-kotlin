import java.util.*
import java.util.concurrent.BlockingQueue

fun main() {
    fun part1(input: String): Int {
        val elves = input.split("\n\n")

        var mostCalories = 0
        for (elf in elves) {
            var totalCalories = 0;
            val calories = elf.split("\n")
            for (calorie in calories) {
                totalCalories += Integer.parseInt(calorie)
            }
            mostCalories = Math.max(mostCalories, totalCalories)
        }

        return mostCalories
    }

    fun part2(input: String): Int {
        val elves = input.split("\n\n")

        var orderedCalories = TreeSet<Int>()
        for (elf in elves) {
            var totalCalories = 0;
            val calories = elf.split("\n")
            for (calorie in calories) {
                totalCalories += Integer.parseInt(calorie)
            }
            orderedCalories.add(totalCalories)
        }
        val highest = orderedCalories.pollLast()
        val nextHighest = orderedCalories.pollLast()
        val lastHighest = orderedCalories.pollLast()
        return highest + nextHighest + lastHighest
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
