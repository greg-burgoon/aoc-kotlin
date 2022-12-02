import java.util.*

fun main() {
    val conclusionMap = mapOf(
        "A X" to 3,
        "A Y" to 6,
        "A Z" to 0,
        "B X" to 0,
        "B Y" to 3,
        "B Z" to 6,
        "C X" to 6,
        "C Y" to 0,
        "C Z" to 3
    )

    val winLoseDrawMap = mapOf(
        "A X" to "A Z",
        "A Y" to "A X",
        "A Z" to "A Y",
        "B X" to "B X",
        "B Y" to "B Y",
        "B Z" to "B Z",
        "C X" to "C Y",
        "C Y" to "C Z",
        "C Z" to "C X"
    )

    val moveMap = mapOf(
        "X" to 1,
        "Y" to 2,
        "Z" to 3
    )


    fun part1(input: String): Int {
        val lines = input.split("\n")
        val winValues = lines.stream().map {
            val move = it.split(" ").get(1)
            conclusionMap.getOrDefault(it, 0).plus(moveMap.getOrDefault(move, 0))
        }.toList().sum()
        return winValues
    }

    fun part2(input: String): Int {
        val lines = input.split("\n")
        val winValues = lines.stream().map {
            winLoseDrawMap.getOrDefault(it, "")
        }.map {
            val move = it.split(" ").get(1)
            conclusionMap.getOrDefault(it, 0).plus(moveMap.getOrDefault(move, 0))
        }.toList().sum()
        return winValues
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}
