fun main() {
    data class Node(val value: Long, var visited: Boolean, var leftNode: Node? = null, var rightNode: Node? = null) {
        fun printLeft(): String {
            var trackNode = leftNode
            var s = this.value.toString() + ", "
            while (trackNode != this) {
                s = s + trackNode?.value!! + ", "
                trackNode = trackNode?.leftNode!!
            }
            return s
        }

        fun printRight(): String {
            var trackNode = rightNode
            var s = this.value.toString() + ", "
            while (trackNode != this) {
                s = s + trackNode?.value!! + ", "
                trackNode = trackNode?.rightNode!!
            }
            return s
        }
    }

    fun getMixedList(originalList: List<Node>, rotations: Int): Node {
        var startNode = originalList.first()
        var zeroNode = Node(0, false)
        var doubleLinkedCircularList = originalList.reduce { acc, node ->
            acc.rightNode = node
            node.leftNode = acc
            node
        }
        startNode.leftNode = doubleLinkedCircularList
        doubleLinkedCircularList.rightNode = startNode

        repeat(rotations) {
            for (currentNode in originalList) {
                var numberOfMoves = currentNode?.value!! % (originalList.size - 1)
                if (currentNode.value == 0L) {
                    zeroNode = currentNode
                    continue
                }
                currentNode.leftNode?.rightNode = currentNode.rightNode
                currentNode.rightNode?.leftNode = currentNode.leftNode

                var newPosition = currentNode!!

                for (i in 0..Math.abs(numberOfMoves) - 1) {
                    if (numberOfMoves < 0) {
                        newPosition = newPosition.leftNode!!
                    } else if (numberOfMoves > 0) {
                        newPosition = newPosition.rightNode!!
                    }
                }

                if (numberOfMoves < 0) {
                    newPosition.leftNode?.rightNode = currentNode
                    currentNode.leftNode = newPosition.leftNode
                    newPosition.leftNode = currentNode
                    currentNode.rightNode = newPosition

                } else if (numberOfMoves > 0) {
                    newPosition.rightNode?.leftNode = currentNode
                    currentNode.rightNode = newPosition.rightNode
                    newPosition.rightNode = currentNode
                    currentNode.leftNode = newPosition
                }
            }
        }
        return zeroNode
    }

    fun part1(input: String): Long {
        var originalList = input.split("\n").map { Node(it.toLong(), false) }

        var zeroNode = getMixedList(originalList, 1)

        var sum = 0L
        var trackNode = zeroNode
        repeat(1000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        trackNode = zeroNode
        repeat(2000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        trackNode = zeroNode
        repeat(3000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        return sum
    }

    fun part2(input: String): Long {
        var inputLists = input.split("\n")
        var originalList = inputLists.map { Node((it.toLong()*811589153), false) }

        var zeroNode = getMixedList(originalList, 10)

        var sum = 0L
        var trackNode = zeroNode
        repeat(1000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        trackNode = zeroNode
        repeat(2000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        trackNode = zeroNode
        repeat(3000){
            trackNode = trackNode.rightNode!!
        }
        sum += trackNode.value

        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day20_test")
    val output = part1(testInput)
    check(output== 3L)

    val input = readInput("Day20")
    println(part1(input))
    println(part2(input))
}
