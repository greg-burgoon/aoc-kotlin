enum class Type {
    COMMAND, OUTPUT
}

fun main() {
    val TOTAL_SPACE = 70000000
    val REQUIRED_SPACE = 30000000

    class Node(name: String, parentNode: Node?){
        val childNodes = HashMap<String, Node>()
        var parentNode = parentNode
        var size = -1
        var calculatedSize = -1

        fun isSizeCalculated(): Boolean {
            return calculatedSize != -1
        }

        fun isFile(): Boolean {
            return size != -1
        }

        fun isDirectory(): Boolean {
            return size == -1
        }

        fun getCalcSize(): Int {
            if (isSizeCalculated()) {
                return calculatedSize
            } else if (isFile()) {
                return size
            } else {
                calculatedSize = childNodes.keys.sumOf {
                    childNodes[it]?.getCalcSize()!!
                }
                return calculatedSize
            }
        }

        fun findSubdirectoriesUnderSize(list: MutableList<Node>, sizeLimit: Int) : List<Node>{
            if (isDirectory()  && this.calculatedSize <= sizeLimit) {
                list.add(this)
            }
            childNodes.values.forEach{
                it.findSubdirectoriesUnderSize(list, sizeLimit)
            }
            return list
        }
    }

    fun part1(input: String): Int {
        var rootNode = Node("/", null)
        var currentNode = rootNode
        var lineType = Type.COMMAND

        input.split("\n")
            .drop(1)
            .map {
                var args = it.split(" ").toMutableList()
                args.add(" ")
                args
            }
            .forEach {(firstToken, secondToken, thirdToken) ->
                if (firstToken == "$") {
                    lineType = Type.COMMAND
                } else {
                    lineType = Type.OUTPUT
                }

                if (lineType == Type.COMMAND && secondToken == "cd") {
                    //handle ..
                    if (thirdToken == "..") {
                        currentNode = currentNode.parentNode!!
                    } else {
                        currentNode = currentNode.childNodes.get(thirdToken)!!
                    }

                } else if (lineType == Type.OUTPUT) {
                    val fileSize = firstToken.toIntOrNull()
                    val fileName = secondToken
                    val childNode = Node(fileName, currentNode)
                    childNode.size = fileSize ?: -1
                    currentNode.childNodes.put(fileName, childNode)
                }
            }

        rootNode.getCalcSize()
        var smallDirectories = rootNode.findSubdirectoriesUnderSize(ArrayList<Node>(), 100000)
        return smallDirectories.sumOf {
            it.calculatedSize
        }
    }

    fun part2(input: String): Int {
        var rootNode = Node("/", null)
        var currentNode = rootNode
        var lineType = Type.COMMAND

        input.split("\n")
            .drop(1)
            .map {
                var args = it.split(" ").toMutableList()
                args.add(" ")
                args
            }
            .forEach {(firstToken, secondToken, thirdToken) ->
                if (firstToken == "$") {
                    lineType = Type.COMMAND
                } else {
                    lineType = Type.OUTPUT
                }

                if (lineType == Type.COMMAND && secondToken == "cd") {
                    //handle ..
                    if (thirdToken == "..") {
                        currentNode = currentNode.parentNode!!
                    } else {
                        currentNode = currentNode.childNodes.get(thirdToken)!!
                    }

                } else if (lineType == Type.OUTPUT) {
                    val fileSize = firstToken.toIntOrNull()
                    val fileName = secondToken
                    val childNode = Node(fileName, currentNode)
                    childNode.size = fileSize ?: -1
                    currentNode.childNodes.put(fileName, childNode)
                }
            }
        
        val remainingSpace = TOTAL_SPACE - rootNode.getCalcSize()
        var allDirectories = rootNode.findSubdirectoriesUnderSize(ArrayList<Node>(), Int.MAX_VALUE)
        var smallestFreeDirectory = 0

        allDirectories
            .map { it.calculatedSize }
            .sortedDescending()
            .forEach {
                if (remainingSpace + it > REQUIRED_SPACE) {
                    smallestFreeDirectory = it
                }
            }

        return smallestFreeDirectory
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val output = part1(testInput)
    check(output == 95437)

    val outputTwo = part2(testInput)
    check(outputTwo == 24933642)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))

}
