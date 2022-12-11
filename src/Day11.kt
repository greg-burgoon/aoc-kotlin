fun main() {

    class Monkey (startingItems: MutableList<Long>, operation: String, testDivisor: Int, testTruth: Int, testFalse: Int) {
        val operation = operation
        var items = startingItems
        val testDivisor = testDivisor
        val testTrueMonkeyNumber = testTruth
        val testFalseMonkeyNumber = testFalse
        var inspectionCount = 0L
        lateinit var worrySubsidingFunction: (Long) -> Long

        fun inspectNextItem(): Boolean {
            if (items.size == 0) {
                return false
            }
            inspectionCount++
            var amount = operation.split(" ").last().toLongOrNull()
            if (amount == null) {
                amount = items.first()
            }

            if (operation.contains("*")) {
                var newItem = worrySubsidingFunction(items.first() * amount)
                items.removeFirst()
                items.add(0, newItem)
            }
            if (operation.contains("+")) {
                var newItem = worrySubsidingFunction(items.first() + amount)
                items.removeFirst()
                items.add(0, newItem)
            }
            return true
        }
        fun determineNextMonkey(): Int {
            if ((items.first() % testDivisor) == 0L) {
                return testTrueMonkeyNumber
            } else {
                return testFalseMonkeyNumber
            }
        }

        fun receiveItem(item: Long) {
            items.add(item)
        }
    }

    fun part1(input: String): Long {
        var monkeys = input.split("\n\n")
            .map {
                var items = mutableListOf<Long>()
                var operation = ""
                var testDivisor = -1
                var testTrueMonkeyNumber = -1
                var testFalseMonkeyNumber = -1
                it.split("\n")
                    .drop(1)
                    .forEachIndexed { index, s ->
                        if (index == 0) {
                            items = s.plus(",").split(" ").filter {
                                it.contains(",")
                            }.map {
                                it.dropLast(1).toLong()
                            }.toMutableList()
                        } else if (index == 1) {
                            operation = s
                        } else if (index == 2) {
                            testDivisor = s.split(" ").last().toInt()
                        } else if (index == 3) {
                            testTrueMonkeyNumber  = s.split(" ").last().toInt()
                        } else if (index == 4) {
                            testFalseMonkeyNumber  = s.split(" ").last().toInt()
                        }
                }
                Monkey(items, operation, testDivisor, testTrueMonkeyNumber, testFalseMonkeyNumber)
            }
        monkeys.forEach {
            it.worrySubsidingFunction = { it /3L }
        }
        repeat(20){
            monkeys.forEach {
                while (it.inspectNextItem()) {
                    var newMonkeyIndex = it.determineNextMonkey()
                    monkeys[newMonkeyIndex].receiveItem(it.items.removeFirst())
                }
            }
        }
        return monkeys.map { it.inspectionCount }.sortedDescending().take(2).reduce {
                acc, i -> i*acc
        }
    }

    fun part2(input: String): Long {
        var monkeys = input.split("\n\n")
            .map {
                var items = mutableListOf<Long>()
                var operation = ""
                var testDivisor = -1
                var testTrueMonkeyNumber = -1
                var testFalseMonkeyNumber = -1
                it.split("\n")
                    .drop(1)
                    .forEachIndexed { index, s ->
                        if (index == 0) {
                            items = s.plus(",").split(" ").filter {
                                it.contains(",")
                            }.map {
                                it.dropLast(1).toLong()
                            }.toMutableList()
                        } else if (index == 1) {
                            operation = s
                        } else if (index == 2) {
                            testDivisor = s.split(" ").last().toInt()
                        } else if (index == 3) {
                            testTrueMonkeyNumber  = s.split(" ").last().toInt()
                        } else if (index == 4) {
                            testFalseMonkeyNumber  = s.split(" ").last().toInt()
                        }
                    }
                Monkey(items, operation, testDivisor, testTrueMonkeyNumber, testFalseMonkeyNumber)
            }

        var worryingSpace = monkeys.map { it.testDivisor.toLong() }.reduce(Long::times)
        monkeys.forEach {
            it.worrySubsidingFunction = { it % worryingSpace }
        }
        repeat(10000){
            monkeys.forEach {
                while (it.inspectNextItem()) {
                    var newMonkeyIndex = it.determineNextMonkey()
                    monkeys[newMonkeyIndex].receiveItem(it.items.removeFirst())
                }
            }
        }
        return monkeys.map { it.inspectionCount }.sortedDescending().take(2).reduce {
                acc, i -> i*acc
        }
    }

    val testInput = readInput("Day11_test")
    val output = part1(testInput)
    check(output == 10605L)

    val outputTwo = part2(testInput)
    check(outputTwo == 2713310158)

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))

}
