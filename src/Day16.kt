fun main() {
    class Node(name: String, flowRate: Int, adjacentValves: MutableList<Node> = mutableListOf<Node>()) {
        val name = name
        val flowRate = flowRate
        var adjacentValves = adjacentValves
    }

    data class State(val currentNode: String, val openValves: List<String>, var accumulatedPressure: Long, val timeRemaining: Long, val previousState: State?) {
    }

    data class ElephantState(val elephantNode: String, val myState: State) {

    }

    fun initMap(input: String): MutableMap<String, Node> {
        val valveDescriptions = input.split("\n")
        var nodeMap = mutableMapOf<String, Node>()
        val valveRegex = "Valve [A-Z][A-Z]".toRegex()
        val flowRateRegex = "[0-9]+".toRegex()
        valveDescriptions.forEach {
            val valveName = valveRegex.find(it)?.value?.takeLast(2)!!
            val flowRate = flowRateRegex.find(it)?.value!!
            nodeMap.put(valveName, Node(valveName, flowRate.toInt()))
        }

        valveDescriptions.forEach {
            val valveName = valveRegex.find(it)?.value?.takeLast(2)!!
            val adjacentValves = it.split(",").map { token ->
                nodeMap.get(token.takeLast(2))!!
            }
            nodeMap.get(valveName)?.adjacentValves?.addAll(adjacentValves)!!
        }
        return nodeMap
    }


    fun findEndStatesForSinglePerson(nodeMap: MutableMap<String, Node>, time: Long): MutableList<State> {
        val startState = State("AA", mutableListOf<String>(), 0L, time, null)

        var stack = ArrayDeque<State>()
        stack.add(startState)

        var maxMap = mutableMapOf<String, Long>()

        var endStates = mutableListOf<State>()
        while (stack.isNotEmpty()) {
            var currentState = stack.removeFirst()
            if (currentState.timeRemaining <= 0L) {
                endStates.add(currentState)
                continue
            }
            var additionalPressure = currentState.openValves.sumOf { nodeMap?.get(it)?.flowRate!! }
            var potentialMax = currentState.accumulatedPressure + additionalPressure
            if (potentialMax >= maxMap?.getOrDefault(currentState.currentNode, 0L)!!) {
                maxMap.put(currentState.currentNode, potentialMax)
                if (nodeMap.get(currentState.currentNode)?.flowRate!! > 0 && !currentState.openValves.contains(
                        currentState.currentNode
                    )
                ) {
                    var openValves = currentState.openValves.toMutableList()
                    openValves.add(currentState.currentNode)
                    var newState = State(
                        currentState.currentNode,
                        openValves,
                        potentialMax,
                        (currentState.timeRemaining - 1),
                        currentState
                    )
                    stack.add(newState)
                }

                nodeMap?.get(currentState.currentNode)?.adjacentValves?.forEach {
                    var openValves = currentState.openValves.toMutableList()
                    var newState = State(
                        it.name,
                        openValves,
                        potentialMax,
                        (currentState.timeRemaining - 1),
                        currentState
                    )
                    stack.add(newState)
                }
            }
        }
        return endStates
    }

    fun part1(input: String): Long {
        var nodeMap = initMap(input)
        return findEndStatesForSinglePerson(nodeMap, 30L).maxOf { it.accumulatedPressure }
    }


    fun part2(input: String): Long {
        var nodeMap = initMap(input)

        var endStatesSingle = findEndStatesForSinglePerson(nodeMap, 26L)

        var successPaths = mutableListOf<ArrayDeque<State>>()
        for(state in endStatesSingle) {
            var successPath = ArrayDeque<State>()
            var currentState = state
            successPath.add(currentState)
            while (currentState.previousState != null) {
                currentState = currentState?.previousState!!
                successPath.addFirst(currentState)
            }
            successPaths.add(successPath)
        }

        var maxMap = mutableMapOf<String, Long>()
        var endStates = mutableListOf<ElephantState>()
        for (path in successPaths) {
            var stack = ArrayDeque<ElephantState>()
            var startState = path?.removeFirst()!!
            var startElephantState = ElephantState(startState.currentNode, startState)
            stack.add(startElephantState)
            var nextState = path?.removeFirst()!!
            while (stack.isNotEmpty()) {
                var currentState = stack.removeFirst()
                if (currentState.myState.timeRemaining <= 0L) {
                    endStates.add(currentState)
                    continue
                }
                if (currentState.myState.timeRemaining == nextState.timeRemaining) {
                    nextState = path?.removeFirst()!!
                }


                var additionalPressure = currentState.myState.openValves.sumOf { nodeMap?.get(it)?.flowRate!! }
                var potentialMax = currentState.myState.accumulatedPressure + additionalPressure
                if (potentialMax >= maxMap?.getOrDefault(currentState.elephantNode, 0L)!!) {
                    maxMap.put(currentState.elephantNode, potentialMax)
                    if (nodeMap.get(currentState.elephantNode)?.flowRate!! > 0 && !currentState.myState.openValves.contains(
                            currentState.elephantNode
                        ) && currentState.elephantNode != currentState.myState.currentNode
                    ) {
                        var openValves = nextState.openValves.toMutableList()
                        openValves.add(currentState.elephantNode)
                        nextState.accumulatedPressure = potentialMax
                        var newState = ElephantState(
                            currentState.elephantNode,
                            nextState
                        )
                        stack.add(newState)
                    }

                    nodeMap?.get(currentState.elephantNode)?.adjacentValves?.forEach {
                        nextState.accumulatedPressure = potentialMax
                        var newState = ElephantState(
                            it.name,
                            nextState
                        )
                        stack.add(newState)
                    }
                }
            }
        }
        return 0
//
//        var bestStates = endStates
//        return bestStates.maxOf { it.accumulatedPressure }
        //dopart 2

//        var nodeMap = initMap(input)
//
//        val startState = ElephantState("AA", "AA", mutableListOf<String>(), 0L, 26L)
//
//        var stack = ArrayDeque<ElephantState>()
//        stack.add(startState)
//
//        var maxMap = mutableMapOf<String, Long>()
//
//        var endStates = mutableListOf<ElephantState>()
//        while (stack.isNotEmpty()) {
//            var currentState = stack.removeFirst()
//            if (currentState.timeRemaining <= 0L) {
//                endStates.add(currentState)
//                continue
//            }
//            var additionalPressure = currentState.openValves.sumOf { nodeMap?.get(it)?.flowRate!! }
//            var potentialMax = currentState.accumulatedPressure + additionalPressure
//            if (potentialMax >= maxMap?.getOrDefault(currentState.currentNode, 0L)!!) {
//                maxMap.put(currentState.currentNode, potentialMax)
//                //we both open a valve
//                if (nodeMap.get(currentState.currentNode)?.flowRate!! > 0 &&
//                    !currentState.openValves.contains(currentState.currentNode)
//                    && nodeMap.get(currentState.elephantNode)?.flowRate!! > 0 &&
//                    !currentState.openValves.contains(currentState.elephantNode)
//                    && !currentState.currentNode.equals(currentState.elephantNode)) {
//                    var openValves = currentState.openValves.toMutableList()
//                    openValves.add(currentState.currentNode)
//                    openValves.add(currentState.elephantNode)
//                    var newState = ElephantState(currentState.currentNode, currentState.elephantNode, openValves, potentialMax, (currentState.timeRemaining-1))
//                    stack.add(newState)
//                }
//
//                //Only I open valve
//                if (nodeMap.get(currentState.currentNode)?.flowRate!! > 0 &&
//                    !currentState.openValves.contains(currentState.currentNode)) {
//                    var openValves = currentState.openValves.toMutableList()
//                    openValves.add(currentState.currentNode)
//
//                    nodeMap?.get(currentState.elephantNode)?.adjacentValves?.forEach {
//                        var newState = ElephantState(
//                            currentState.currentNode,
//                            it.name,
//                            openValves,
//                            potentialMax,
//                            (currentState.timeRemaining - 1)
//                        )
//                        stack.add(newState)
//                    }
//                }
//
//
//
//                //elephant opens valve
//                if (nodeMap.get(currentState.elephantNode)?.flowRate!! > 0 &&
//                        !currentState.openValves.contains(currentState.elephantNode)) {
//                    var openValves = currentState.openValves.toMutableList()
//                    openValves.add(currentState.elephantNode)
//
//                    nodeMap?.get(currentState.currentNode)?.adjacentValves?.forEach {
//                        var newState = ElephantState(
//                            it.name,
//                            currentState.elephantNode,
//                            openValves,
//                            potentialMax,
//                            (currentState.timeRemaining - 1)
//                        )
//                        stack.add(newState)
//                    }
//                }
//
//                //we both move
//                nodeMap?.get(currentState.currentNode)?.adjacentValves?.forEach { adjacentNode ->
//                    var openValves = currentState.openValves.toMutableList()
//                    nodeMap?.get(currentState.elephantNode)?.adjacentValves?.forEach { elephantAdjacentNode ->
//                        var newState = ElephantState(
//                            adjacentNode.name,
//                            elephantAdjacentNode.name,
//                            openValves,
//                            potentialMax,
//                            (currentState.timeRemaining - 1)
//                        )
//                        stack.add(newState)
//                    }
//                }
//            }
//        }
//        return endStates.maxOf { it.accumulatedPressure }
    }

    val testInput = readInput("Day16_test")
    val output = part1(testInput)
    check(output == 1651L)

    val outputTwo = part2(testInput)
    check(outputTwo == 1707L)

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))

}
