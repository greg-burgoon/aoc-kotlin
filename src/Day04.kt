
fun main() {
    fun part1(input: String): Int {
        return input.split("\n")
            .map {
                var rangeOne = it.split(",")[0].split("-")
                var rangeTwo = it.split(",")[1].split("-")
                val setOne = sequence{yieldAll(rangeOne[0].toInt() .. rangeOne[1].toInt())}.toMutableSet()
                val setTwo = sequence{yieldAll(rangeTwo[0].toInt() .. rangeTwo[1].toInt())}.toMutableSet()
                val setOneSize = setOne.size
                val setTwoSize = setTwo.size
                setOne.retainAll(setTwo)
                if (setOne.size == setOneSize || setOne.size == setTwoSize)
                    1
                else
                    0
            }.sumOf {
                it
            }
    }

    fun part2(input: String): Int {
        return input.split("\n")
            .map {
                var rangeOne = it.split(",")[0].split("-")
                var rangeTwo = it.split(",")[1].split("-")
                val setOne = sequence{yieldAll(rangeOne[0].toInt() .. rangeOne[1].toInt())}.toMutableSet()
                val setTwo = sequence{yieldAll(rangeTwo[0].toInt() .. rangeTwo[1].toInt())}.toMutableSet()
                val setOneSize = setOne.size
                val setTwoSize = setTwo.size
                setOne.retainAll(setTwo)
                if (setOne.size > 0)
                    1
                else
                    0
            }.sumOf {
                it
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    val output = part1(testInput)
    check(output== 2)

    val outputTwo = part2(testInput)
    check(outputTwo== 4)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}
