fun main() {
    data class Node(val name: String, var value: String? = null, var dependencies: MutableList<String> = mutableListOf<String>(), var operation: String = "")

    fun createNodeMap(input: String): MutableMap<String, Node> {
        var nodeMap = mutableMapOf<String, Node>()
        input.split("\n").forEach {
            var nameValue = it.split(": ")
            var node = Node(nameValue[0])
            if (nameValue[1].toLongOrNull() != null) {
                node.value = nameValue[1]
            } else {
                var deps = nameValue[1].split(" ")
                node.dependencies.add(deps[0])
                node.dependencies.add(deps[2])
                node.operation = deps[1]
            }
            nodeMap.put(node.name, node)
        }
        return nodeMap
    }

    fun resolveDependencies(rootNode: Node, nodeMap: MutableMap<String, Node>) {
        var history = ArrayDeque<Node>()
        history.add(rootNode)
        while (!history.isEmpty()) {
            var currentNode = history.last()
            var deps = currentNode.dependencies.map { nodeMap.get(it)!! }.toList()
            if (deps.isNotEmpty() && deps.filter { it.value == null }.isEmpty()) {
                if (deps.filter { it.value?.toLongOrNull() == null }.isNotEmpty()) {
                    currentNode.value = "(" + deps[0].value + currentNode.operation + deps[1].value + ")"
                } else {
                    currentNode.value =
                        currentNode.operation.let {
                            when (it) {
                                "+" -> { one: Long, two: Long ->
                                    one + two
                                }

                                "-" -> { one: Long, two: Long ->
                                    one - two
                                }

                                "*" -> { one: Long, two: Long ->
                                    one * two
                                }

                                else -> { one: Long, two: Long ->
                                    one / two
                                }
                            }
                        }(deps[0].value?.toLong()!!, deps[1].value?.toLong()!!).toString()
                }
            }

            var value = currentNode.value
            if (value != null) {
                history.removeLast()
            } else {
                history.add(deps[0])
                history.add(deps[1])
            }
        }
    }

    fun part1(input: String): Long {

        var nodeMap = createNodeMap(input)

        var rootNode = nodeMap["root"]!!

        resolveDependencies(rootNode, nodeMap)

        return rootNode.value?.toLong()!!
    }

    fun solveForHuman(rootNode: Node, nodeMap: MutableMap<String, Node>): Long {
        var history = ArrayDeque<Node>()
        history.add(rootNode)
//        while (!history.isEmpty()) {
//            var currentNode = history.last()
//
//        }
        return 0L
    }

    fun part2(input: String): Long {

        var nodeMap = createNodeMap(input)

        var rootNode = nodeMap["root"]!!
        rootNode.operation = "="

        var humanNode = nodeMap["humn"]!!
        humanNode.value = "humn"

        resolveDependencies(rootNode, nodeMap)

        return solveForHuman(rootNode, nodeMap)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day21_test")
    val output = part1(testInput)
    check(output== 152L)

    val input = readInput("Day21")
    println(part1(input))
    println(part2(input))
}
