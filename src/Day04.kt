
fun main() {
    fun part1(input: String): Int {
        return input.split("\n")
            .map {
                var rangeOne = it.split(",").get(0).split("-")
                var rangeTwo = it.split(",").get(1).split("-")
                val setOne = generateSequence(rangeOne.get(0).toInt()) {
                    if (it < rangeOne.get(1).toInt())
                        it + 1
                    else
                        null
                }.toMutableSet()
                val setTwo = generateSequence(rangeTwo.get(0).toInt()) {
                    if (it < rangeTwo.get(1).toInt())
                        it + 1
                    else
                        null
                }.toMutableSet()
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
                var rangeOne = it.split(",").get(0).split("-")
                var rangeTwo = it.split(",").get(1).split("-")
                val setOne = generateSequence(rangeOne.get(0).toInt()) {
                    if (it < rangeOne.get(1).toInt())
                        it + 1
                    else
                        null
                }.toMutableSet()
                val setTwo = generateSequence(rangeTwo.get(0).toInt()) {
                    if (it < rangeTwo.get(1).toInt())
                        it + 1
                    else
                        null
                }.toMutableSet()
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
