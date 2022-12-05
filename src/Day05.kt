import java.util.Queue

fun main() {
    fun part1(input: String, numberOfStacks: Int): String {
        val inputs = input.split("\n\n")
        var stacks = ArrayList<ArrayDeque<Char>>()
        for (i in 0..numberOfStacks-1) {
            stacks.add(ArrayDeque(1))
        }
        inputs.get(0)
            .split("\n")
            .map {
                var count = 0;
                it.toCharArray()
                    .toList()
                    .windowed(3, 4)
                    .forEach {
                        val letter = it.get(1)
                        if (letter != ' ' && letter.isLetter()) {
                            stacks.get(count).add(letter)
                        }
                        count++
                    }
            }
        for (i in 0..numberOfStacks-1) {
            stacks.get(i).reverse()
        }
        inputs.get(1)
            .split("\n")
            .map {
                it.split("\\D+".toRegex())
            }.forEach {
                val amount = it.get(1).toInt()
                val fromStack = stacks.get(it.get(2).toInt()-1)
                val toStack = stacks.get(it.get(3).toInt()-1)
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
        var stacks = ArrayList<ArrayDeque<Char>>()
        for (i in 0..numberOfStacks-1) {
            stacks.add(ArrayDeque(1))
        }
        inputs.get(0)
            .split("\n")
            .map {
                var count = 0;
                it.toCharArray()
                    .toList()
                    .windowed(3, 4)
                    .forEach {
                        val letter = it.get(1)
                        if (letter != ' ' && letter.isLetter()) {
                            stacks.get(count).add(letter)
                        }
                        count++
                    }
            }
        for (i in 0..numberOfStacks-1) {
            stacks.get(i).reverse()
        }
        inputs.get(1)
            .split("\n")
            .map {
                it.split("\\D+".toRegex())
            }.forEach {
                val amount = it.get(1).toInt()
                val fromStack = stacks.get(it.get(2).toInt()-1)
                val toStack = stacks.get(it.get(3).toInt()-1)
                val tempStack = ArrayDeque<Char>()
                for (index in fromStack.count()-1 downTo  fromStack.count()-amount) {
                    tempStack.add(fromStack.removeAt(index))
                }
                toStack.addAll(tempStack.reversed())
            }
        return stacks.map {
            it.lastOrNull()
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
