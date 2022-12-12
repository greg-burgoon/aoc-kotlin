fun main() {

    class Node(elevation: Char) {
        val elevation = elevation
        var distance = Int.MAX_VALUE
        var adjacentNodes = ArrayList<Node>()

        fun canTraverseTo(other: Node): Boolean {
            var currentElevation = this.elevation
            if (this.elevation == 'S') {
                currentElevation = 'a'
            }
            if (this.elevation == 'E') {
                currentElevation = 'z'
            }

            var otherElevation = other.elevation
            if (other.elevation == 'S') {
                otherElevation = 'a'
            }
            if (other.elevation == 'E') {
                otherElevation = 'z'
            }
            return otherElevation.code+1 >= currentElevation.code
        }
    }

    fun getDistanceToStartNode(nodeMap: MutableList<MutableList<Node>>): Node {
        nodeMap.forEachIndexed { row, nodes ->
            nodes.forEachIndexed { column, node ->
                var upNode = nodeMap.getOrNull(row - 1)?.get(column)
                if (upNode != null && node.canTraverseTo(upNode)) {
                    node.adjacentNodes.add(upNode)
                }
                var downNode = nodeMap.getOrNull(row + 1)?.get(column)
                if (downNode != null && node.canTraverseTo(downNode)) {
                    node.adjacentNodes.add(downNode)
                }
                var leftNode = nodeMap[row].getOrNull(column - 1)
                if (leftNode != null && node.canTraverseTo(leftNode)) {
                    node.adjacentNodes.add(leftNode)
                }
                var rightNode = nodeMap[row].getOrNull(column + 1)
                if (rightNode != null && node.canTraverseTo(rightNode)) {
                    node.adjacentNodes.add(rightNode)
                }
            }
        }

        var allNodes = nodeMap.flatten().toMutableList()
        var startNode = allNodes.filter { it.elevation == 'S' }.first()
        var endNode = allNodes.filter { it.elevation == 'E' }.first()
        endNode.distance = 0

        var stack = ArrayDeque<Node>()
        stack.add(endNode)
        while (stack.isNotEmpty()) {
            var currentNode = stack.removeFirst()
            currentNode.adjacentNodes.filter { it.distance > currentNode.distance + 1 }.forEach {
                it.distance = currentNode.distance + 1
                stack.add(it)
            }
        }
        return startNode
    }

    fun part1(input: String): Int {
        var nodeMap = mutableListOf<MutableList<Node>>()
        input.split("\n")
            .forEachIndexed { row, s ->
                nodeMap.add(mutableListOf<Node>())
                s.forEachIndexed { column, c ->
                    nodeMap[row].add(Node(c))
                }
            }

        var startNode = getDistanceToStartNode(nodeMap)

        return startNode.distance
    }

    fun part2(input: String): Int {
        var nodeMap = mutableListOf<MutableList<Node>>()
        input.split("\n")
            .forEachIndexed { row, s ->
                nodeMap.add(mutableListOf<Node>())
                s.forEachIndexed { column, c ->
                    nodeMap[row].add(Node(c))
                }
            }

        var startNode = getDistanceToStartNode(nodeMap)

        return nodeMap.flatten().filter { it.elevation == 'a' || it.elevation == 'S' }.sortedByDescending { it.distance }.last().distance
    }

    val testInput = readInput("Day12_test")
    val output = part1(testInput)
    check(output == 31)

    val outputTwo = part2(testInput)
    check(outputTwo == 29)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))

}
