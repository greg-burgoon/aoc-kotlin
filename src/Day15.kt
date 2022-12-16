import java.util.*

fun main() {
    class Point (x: Long, y: Long) {
        val x = x
        val y = y

        fun getDistance(other: Point): Long {
            return Math.abs(other.x-this.x) + Math.abs(other.y-this.y)
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Point

            if (x != other.x) return false
            if (y != other.y) return false

            return true
        }

        override fun hashCode(): Int {
            var result = x.hashCode()
            result = 31 * result + y.hashCode()
            return result
        }
    }

    fun part1(input: String, rowInQuestion: Long): Long {
        val regexX = "x=-?[0-9]+".toRegex()
        val regexY = "y=-?[0-9]+".toRegex()
        val sensorBeaconList = input.split("\n")
            .map { lineString ->
                lineString.split(": ").map { coordString ->
                    var xCoord = regexX.find(coordString)?.value?.removePrefix("x=")?.toLong()!!
                    var yCoord = regexY.find(coordString)?.value?.removePrefix("y=")?.toLong()!!
                    Point(xCoord, yCoord)
                }.toList()
            }
        var beaconList = sensorBeaconList.map {
            it[1]
        }

        var sensorDistanceList = sensorBeaconList.map { (sensor, beacon) ->
            val distance = sensor.getDistance(beacon)
            listOf(sensor, distance)
        }

        var sensorPoints = sensorDistanceList.map {
            it[0] as Point
        }

        var distanceList = sensorDistanceList.map {
            it[1] as Long
        }

        var maxX = sensorPoints.maxOf {
            it.x
        }
        var minX = sensorPoints.minOf {
            it.x
        }
        var maxDistance = distanceList.max()

        maxX = maxX + maxDistance
        minX = minX - maxDistance

        var count = 0L
        for (x in minX .. maxX){
            var comparisonPoint = Point(x, rowInQuestion)
            var isInRangeOfSensor = false
            sensorDistanceList.forEach { (sensor, distance) ->
                if (sensor is Point && distance is Long) {
                    isInRangeOfSensor = isInRangeOfSensor ||
                            (sensor.getDistance(comparisonPoint) <= distance &&
                                    !beaconList.contains(comparisonPoint))
                }
            }
            if (isInRangeOfSensor) {
                count++
            }
        }
        return count
    }

    fun part2(input: String, minX: Long, maxX: Long, minY: Long, maxY: Long): Long {
        val regexX = "x=-?[0-9]+".toRegex()
        val regexY = "y=-?[0-9]+".toRegex()
        val sensorDistanceList = input.split("\n")
            .map { lineString ->
                lineString.split(": ").map { coordString ->
                    var xCoord = regexX.find(coordString)?.value?.removePrefix("x=")?.toLong()!!
                    var yCoord = regexY.find(coordString)?.value?.removePrefix("y=")?.toLong()!!
                    Point(xCoord, yCoord)
                }.toList()
            }.map { (sensor, beacon) ->
                val distance = sensor.getDistance(beacon)
                listOf(sensor, distance)
            }

        var rangeMap = mutableMapOf<Long, MutableList<LongRange>>()
        for (y in minY .. maxY) {
                sensorDistanceList.forEach { (sensor, distance) ->
                    if (sensor is Point && distance is Long && Math.abs(sensor.y - y) <= distance) {
                        var length = (distance*2 +1) - (2*Math.abs(sensor.y - y))
                        var minRange = sensor.x - length/2
                        var maxRange = sensor.x + length/2
                        var rangeList = rangeMap.getOrDefault(y, mutableListOf<LongRange>())
                        rangeList.add(minRange .. maxRange)
                        rangeMap.put(y, rangeList)
                    }
                }
        }

        for (y in minY .. maxY) {
            var rangeIterator = rangeMap.get(y)!!.sortedBy {
               it.first
            }.iterator()
            var previousRange = rangeIterator.next()
            var previousMax = -1L
            while (rangeIterator.hasNext()) {
                var nextRange = rangeIterator.next()
                previousMax = Math.max(previousRange.last, previousMax)
                if (previousMax < nextRange.first) {
                    return ((previousRange.last+1) * 4000000) + y
                }
                previousRange = nextRange
            }
        }
        return 0 //(deadBeacon.x * 4000000) + deadBeacon.y
    }

    val testInput = readInput("Day15_test")
    val output = part1(testInput, 10)
    check(output == 26L)

    val outputTwo = part2(testInput, 0, 20, 0, 20)
    check(outputTwo == 56000011L)

    val input = readInput("Day15")
    println(part1(input, 2000000))
    println(part2(input, 0, 4000000, 0, 4000000))

}
