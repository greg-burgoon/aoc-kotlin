fun main() {


    fun part1(input: String): Int {
        var row = 0
        var column = 0

        var leftLists = ArrayList<ArrayList<Int>>()
        var upLists = ArrayList<ArrayList<Int>>()
        var rightLists = ArrayList<ArrayList<Int>>()
        var downLists  = ArrayList<ArrayList<Int>>()
        rightLists.add(ArrayList<Int>())
        input.toCharArray().forEach {
            if (row == 0 && it.toString() != "\n") {
                downLists.add(ArrayList<Int>())
            }
            if (it.toString() == "\n") {
                row++
                column = 0
                rightLists.add(ArrayList<Int>())
            } else {
                var number = it.toString().toInt()
                rightLists[row].add(number)
                downLists[column].add(number)
                column++
            }
        }

        row = 0
        column = 0
        leftLists.add(ArrayList<Int>())
        
        return input.toCharArray().map {
            var result = 0
            if (row == 0 && it.toString() != "\n") {
                upLists.add(ArrayList<Int>())
            }
            if (it.toString() == "\n") {
                row++
                column = 0
                leftLists.add(ArrayList<Int>())
            } else {
                var number = it.toString().toInt()
                rightLists[row].remove(number)
                downLists[column].remove(number)

                //do check
                if (column == 0 ||
                    column == downLists.size-1 ||
                    row == 0 ||
                    row == rightLists.size-1 ||
                    Math.min(leftLists[row].maxOrNull()!!, rightLists[row].maxOrNull()!!) < number ||
                    Math.min(upLists[column].maxOrNull()!!, downLists[column].maxOrNull()!!) < number
                ) {
                    result = 1
                }

                leftLists[row].add(number)
                upLists[column].add(number)
                column++
            }
            result
        }.sum()
    }

    fun part2(input: String): Int {
        var row = 0
        var column = 0

        var leftLists = ArrayList<ArrayList<Int>>()
        var upLists = ArrayList<ArrayList<Int>>()
        var rightLists = ArrayList<ArrayList<Int>>()
        var downLists  = ArrayList<ArrayList<Int>>()
        rightLists.add(ArrayList<Int>())
        input.toCharArray().forEach {
            if (row == 0 && it.toString() != "\n") {
                downLists.add(ArrayList<Int>())
            }
            if (it.toString() == "\n") {
                row++
                column = 0
                rightLists.add(ArrayList<Int>())
            } else {
                var number = it.toString().toInt()
                rightLists[row].add(number)
                downLists[column].add(number)
                column++
            }
        }

        row = 0
        column = 0
        leftLists.add(ArrayList<Int>())

        return input.toCharArray().map {
            var result = 0
            if (row == 0 && it.toString() != "\n") {
                upLists.add(ArrayList<Int>())
            }
            if (it.toString() == "\n") {
                row++
                column = 0
                leftLists.add(ArrayList<Int>())
            } else {
                var number = it.toString().toInt()
                rightLists[row].remove(number)
                downLists[column].remove(number)

                //do check
                var leftMaxIndex = 0
                leftLists[row].forEachIndexed { index, i ->
                    if (number <= leftLists[row][index]) {
                        leftMaxIndex = index
                    }
                }
                var rightMaxIndex = rightLists[row].size
                for (i in rightLists[row].indices) {
                    if (number <= rightLists[row][i]) {
                        rightMaxIndex = i+1
                        break
                    }
                }

                var upMaxIndex = 0
                upLists[column].forEachIndexed { index, i ->
                    if (number <= upLists[column][index]) {
                        upMaxIndex = index
                    }
                }
                var downMaxIndex = downLists[column].size
                for (i in downLists[column].indices) {
                    if (number <= downLists[column][i]) {
                        downMaxIndex = i+1
                        break
                    }
                }

                var leftDistance = column - leftMaxIndex
                var rightDistance = rightMaxIndex
                var upDistance = row - upMaxIndex
                var downDistance = downMaxIndex
                result = leftDistance * rightDistance * upDistance * downDistance

                leftLists[row].add(number)
                upLists[column].add(number)
                column++
            }
            result
        }.max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    val output = part1(testInput)
    check(output == 21)

    val outputTwo = part2(testInput)
    check(outputTwo == 8)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))

}
