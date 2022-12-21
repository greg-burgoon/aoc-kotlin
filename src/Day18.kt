import java.util.TreeSet

fun main() {

    fun calculateSurfaceArea(coordList: List<List<Int>>): Int {
        var coordMap = mutableMapOf<String, Int>()
        return coordList.map { listOfCoord ->
            var neighbourDiscount = 0;
            listOfCoord.forEachIndexed { index, i ->
                var coordStringPlus = listOfCoord.toMutableList()
                coordStringPlus[index] = i + 1
                var coordStringMinus = listOfCoord.toMutableList()
                coordStringMinus[index] = i - 1
                neighbourDiscount += coordMap.getOrDefault(coordStringPlus.joinToString(","), 0)
                neighbourDiscount += coordMap.getOrDefault(coordStringMinus.joinToString(","), 0)
            }
            coordMap.put(listOfCoord.joinToString(","), 2)
            neighbourDiscount
        }.fold(0) { acc: Int, neighbourDiscount: Int ->
            acc + 6 - neighbourDiscount
        }
    }


    fun buildAirPocket(rootNode: String, rockMap: Map<String, Boolean>, exteriorSurfaceArea: Int): Int? {
        var visitedNodes = mutableMapOf<String, Boolean>()
        var nodeStack = TreeSet<String>()
        nodeStack.add(rootNode)
        var pocketSurfaceArea = 0

        while (nodeStack.isNotEmpty() && pocketSurfaceArea <= exteriorSurfaceArea+1) {
            var nextString = nodeStack.pollFirst()
            var coord = nextString.split(",").map { it.toInt() }
            coord.let {
                it.forEachIndexed { index, i ->
                    var coordStringPlus = coord.toMutableList()
                    coordStringPlus[index] = i + 1
                    var coordStringMinus = coord.toMutableList()
                    coordStringMinus[index] = i - 1
                    if (!rockMap.getOrDefault(coordStringPlus.joinToString(","), false) && !visitedNodes.getOrDefault(coordStringPlus.joinToString(","), false)) {
                        nodeStack.add(coordStringPlus.joinToString(","))
                    }
                    if (!rockMap.getOrDefault(coordStringMinus.joinToString(","), false) && !visitedNodes.getOrDefault(coordStringMinus.joinToString(","), false)) {
                        nodeStack.add(coordStringMinus.joinToString(","))
                    }
                }
            }
            visitedNodes[nextString] = true
            pocketSurfaceArea = calculateSurfaceArea(visitedNodes.keys.map { it.split(",").map { it.toInt() } })
        }

        if (pocketSurfaceArea <= exteriorSurfaceArea) {
            return pocketSurfaceArea
        } else {
            return null
        }
    }

    fun findAirPockets(coordList: MutableList<List<Int>>, exteriorSurfaceArea: Int): List<Int> {
        var neighbourMap = mutableMapOf<String, Int>()
        var rockMap = mutableMapOf<String, Boolean>()
        coordList.forEach { listOfCoord ->
            listOfCoord.forEachIndexed { index, i ->
                var coordStringPlus = listOfCoord.toMutableList()
                coordStringPlus[index] = i + 1
                var coordStringMinus = listOfCoord.toMutableList()
                coordStringMinus[index] = i - 1
                neighbourMap.compute(coordStringPlus.joinToString(",")) { k: String, v: Int? -> if (v == null) 1 else v + 1 }
                neighbourMap.compute(coordStringMinus.joinToString(",")) { k: String, v: Int? -> if (v == null) 1 else v + 1 }
            }
            rockMap.put(listOfCoord.joinToString(","), true)
        }
        rockMap.forEach{
            neighbourMap.remove(it.key)
        }
        var airPockets = mutableListOf<Int>()
        neighbourMap.filter { it.value >= 3 }.keys.forEach {
            var potentialAirPocket = buildAirPocket(it, rockMap, exteriorSurfaceArea)
            if (potentialAirPocket != null) {
                airPockets.add(potentialAirPocket)
            }
        }
        return airPockets.toList()
    }

    fun part1(input: String): Int {
        var coordList = input.split("\n").map { coordString ->
            coordString.split(",").map {
                it.toInt()
            }
        }
        return calculateSurfaceArea(coordList)
    }

    fun part2(input: String): Int {
        var coordList = input.split("\n").map { coordString ->
            coordString.split(",").map {
                it.toInt()
            }
        }.toMutableList()

        var exteriorSurfaceArea = calculateSurfaceArea(coordList)
        var airPockets = findAirPockets(coordList, exteriorSurfaceArea/2)

        return calculateSurfaceArea(coordList) - airPockets.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day18_test")
    val result = part1(testInput)
    check(result == 64)

    val resultTwo = part2(testInput)
    check(resultTwo == 58)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}
