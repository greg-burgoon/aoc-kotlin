fun main() {

    fun parseStacks(input: String, numberOfStacks: Int): List<ArrayDeque<Char>> {
        var stacks = ArrayList<ArrayDeque<Char>>()
        repeat(numberOfStacks) {
            stacks.add(ArrayDeque<Char>())
        }
        input
            .split("\n")
            .map {
                it.toList()
                    .windowed(3, 4)
                    .forEachIndexed { index, chars ->
                        val letter = chars.get(1)
                        if (letter != ' ' && letter.isLetter()) {
                            stacks.get(index).add(letter)
                        }
                    }
            }
        return stacks.map {
            it.reverse()
            it
        }
    }

    fun parseInstructions(input: String): List<List<Int>> {
        return input
            .split("\n")
            .map {
                it.split("\\D+".toRegex())
                    .filter { it.isNotBlank() }
                    .map {
                        it.toInt()
                    }
            }
    }

    fun part1(input: String, numberOfStacks: Int): String {
        val inputs = input.split("\n\n")
        val stacks = parseStacks(inputs[0], numberOfStacks)
        val instructions = parseInstructions(inputs[1])
        instructions.forEach {(amount, fromIndex, toIndex) ->
                val fromStack = stacks[fromIndex-1]
                val toStack = stacks[toIndex-1]
                for (i in 1..amount) {
                    toStack.add(fromStack.removeLast())
                }
            }
        return stacks.map {
            it.last()
        }.joinToString("")
    }

    fun part2(input: String, numberOfStacks: Int): String {
        val inputs = input.split("\n\n")
        val stacks = parseStacks(inputs[0], numberOfStacks)
        val instructions = parseInstructions(inputs[1])
        instructions.forEach {(amount, fromIndex, toIndex) ->
            val fromStack = stacks[fromIndex-1]
            val toStack = stacks[toIndex-1]
            val tempStack = ArrayDeque<Char>()
            for (index in fromStack.count()-1 downTo  fromStack.count()-amount) {
                tempStack.add(fromStack.removeAt(index))
            }
            toStack.addAll(tempStack.reversed())
        }
        return stacks.map {
            it.last()
        }.joinToString("")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val output = part1(testInput, 3)
    check(output == "CMZ")

    val outputTwo = part2(testInput, 3)
    check(outputTwo == "MCD")

    val input = readInput("Day05")
    println(part1(input, 9))
    println(part2(input, 9))
}
