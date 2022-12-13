fun main() {
    fun constructList(listString: String): MutableList<Any> {
        var stack = ArrayDeque<MutableList<Any>>()
        var currentInteger = ""
        listString.toCharArray().forEach {
            if (it == '[') {
                stack.add(mutableListOf<Any>())
            } else if (it == ']') {
                if (currentInteger.length > 0) {
                    stack.last().add(currentInteger.toInt())
                    currentInteger = ""
                }
                if (stack.size == 1)
                {
                    return stack.first()
                }
                var currentList = stack.removeLast()
                stack.last().add(currentList)
            } else if (it == ',') {
                if (currentInteger.length > 0) {
                    stack.last().add(currentInteger.toInt())
                    currentInteger = ""
                }
            } else {
                currentInteger = currentInteger + it
            }
        }
        return stack.first()
    }

    fun checkLists(listOne: MutableList<*>, listTwo: MutableList<*>): Int {
        val iteratorOne = listOne.stream().iterator()
        val iteratorTwo = listTwo.stream().iterator()
        while(iteratorOne.hasNext() && iteratorTwo.hasNext()) {
            val valueOne = iteratorOne.next()
            val valueTwo = iteratorTwo.next()
            if (valueOne is Int && valueTwo is Int) {
                if (valueOne < valueTwo) {
                    return -1
                } else if (valueOne > valueTwo) {
                    return 1
                }
            } else if (valueOne is MutableList<*> && valueTwo is MutableList<*>) {
                var ordering = checkLists(valueOne, valueTwo)
                if (ordering != 0) {
                    return ordering
                }
            } else {
                if (valueOne is Int && valueTwo is MutableList<*>) {
                    var ordering = checkLists(mutableListOf<Any>(valueOne), valueTwo)
                    if (ordering != 0) {
                        return ordering
                    }
                } else if (valueOne is MutableList<*> && valueTwo is Int) {
                    var ordering = checkLists(valueOne, mutableListOf<Any>(valueTwo))
                    if (ordering != 0) {
                        return ordering
                    }
                }
            }
        }
        var ordering = if (!iteratorOne.hasNext() && iteratorTwo.hasNext()) -1 else if (!iteratorOne.hasNext() && !iteratorTwo.hasNext()) 0 else 1
        return ordering
    }

    fun part1(input: String): Int {
        var lists = input.split("\n\n")
            .map {
                var lists = it.split("\n")
                listOf(lists[0], lists[1])
            }.map { (listOneString, listTwoString) ->
                listOf(constructList(listOneString), constructList(listTwoString))
            }.mapIndexed { index, (listOne, listTwo) ->
                if (checkLists(listOne, listTwo) == -1) index+1 else 0
            }

        return lists.sum()
    }

    fun part2(input: String): Int {
        var lists = input.split("\n\n")
            .map {
                var lists = it.split("\n")
                listOf(lists[0], lists[1])
            }.flatMap { (listOneString, listTwoString) ->
                listOf(constructList(listOneString), constructList(listTwoString))
            }.toMutableList()
            var twoSeparator = mutableListOf(mutableListOf(2))
            var sixSeparator = mutableListOf(mutableListOf(6))
            lists.add(twoSeparator.toMutableList())
            lists.add(sixSeparator.toMutableList())
            lists.sortWith(Comparator { o1, o2 -> checkLists(o1, o2) })

        return (lists.indexOf(twoSeparator.toMutableList())+1) * (lists.indexOf(sixSeparator.toMutableList())+1)
    }

    val testInput = readInput("Day13_test")
    val output = part1(testInput)
    check(output == 13)

    val outputTwo = part2(testInput)
    check(outputTwo == 140)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))

}
