fun main() {
    class Point (x: Int, y: Int) {
        val x = x
        val y = y
    }

    fun drawLine(grid: MutableList<MutableList<Char>>, firstPoint: Point, secondPoint: Point) {
        val isVerticalLine = firstPoint.x == secondPoint.x

        if (isVerticalLine) {
            var maxY = Math.max(firstPoint.y, secondPoint.y)
            var minY = Math.min(firstPoint.y, secondPoint.y)
            for(i in minY .. maxY){
                grid[i][firstPoint.x] = '#'
            }
        } else {
            var maxX = Math.max(firstPoint.x, secondPoint.x)
            var minX = Math.min(firstPoint.x, secondPoint.x)
            for(i in minX .. maxX){
                grid[firstPoint.y][i] = '#'
            }
        }
    }

    fun simulateSand(grid: MutableList<MutableList<Char>>, sandLocation: Point): Int {
        var isTrackingUnit = true
        var currentSandPosition = sandLocation
        var sandUnits = 1
        while(true){
            //launch new Sand?
            if (isTrackingUnit) {
                if (currentSandPosition.y + 1 >= grid.size) {
                    break
                }
                if (grid[currentSandPosition.y+1][currentSandPosition.x] == '.') {
                    currentSandPosition = Point(currentSandPosition.x, currentSandPosition.y+1)
                } else if (currentSandPosition.x - 1 >= 0 && grid[currentSandPosition.y+1][currentSandPosition.x-1] == '.') {
                    currentSandPosition = Point(currentSandPosition.x - 1, currentSandPosition.y + 1)
                } else if (currentSandPosition.x + 1 < grid[0].size && grid[currentSandPosition.y+1][currentSandPosition.x+1] == '.') {
                    currentSandPosition = Point( currentSandPosition.x + 1, currentSandPosition.y + 1)
                } else {
                    if (currentSandPosition.x - 1 < 0) {
                        break
                    } else if (currentSandPosition.x + 1 >= grid[0].size) {
                        break
                    }
                    if (grid[currentSandPosition.y][currentSandPosition.x] == 'O') {
                        break
                    }
                    grid[currentSandPosition.y][currentSandPosition.x] = 'O'
                    isTrackingUnit = false
                }
            } else {
                isTrackingUnit = true
                currentSandPosition = sandLocation
                sandUnits++
            }
        }
        return sandUnits-1
    }

    fun getAllPointsFromInput(input: String): MutableList<Point> {
        var points = input.split("\n")
            .flatMap {
                it.split(" -> ").map {
                    var pair = it.split(",")
                    Point(pair[0].toInt(), pair[1].toInt())
                }
            }.toMutableList()
        return points
    }

    fun createGridFromMinAndMax(
        minY: Int,
        maxY: Int,
        minX: Int,
        maxX: Int
    ): MutableList<MutableList<Char>> {
        var grid = mutableListOf<MutableList<Char>>()
        for (y in minY..maxY) {
            grid.add(mutableListOf<Char>())
            for (x in minX..maxX) {
                grid[y].add('.')
            }
        }
        return grid
    }

    fun createRocks(
        rockLines: List<List<Point>>,
        grid: MutableList<MutableList<Char>>
    ) {
        rockLines.forEach {
            it.windowed(2, 1) { pairs ->
                var firstPoint = pairs[0]
                var secondPoint = pairs[1]
                drawLine(grid, firstPoint, secondPoint)
            }
        }
    }

    fun part1(input: String): Int {
        var points = getAllPointsFromInput(input)
        var minX = points.minOf { it.x }
        var transform = 0 - minX
        var maxX = points.maxOf { it.x } + transform
        minX = 0

        var minY = 0
        var maxY = points.maxOf { it.y }

        var grid = createGridFromMinAndMax(minY, maxY, minX, maxX)

        var rockLines = input.split("\n")
            .map {
                it.split(" -> ").map {
                    var pair = it.split(",")
                    Point(pair[0].toInt()+transform, pair[1].toInt())
                }
            }

        createRocks(rockLines, grid)

        var sandLocation = Point(500+transform, 0)
        var numberOfSandUnits = simulateSand(grid, sandLocation)

        return numberOfSandUnits
    }

    fun part2(input: String): Int {
        var points = getAllPointsFromInput(input)
        var minX = points.minOf { it.x }
        var maxX = points.maxOf { it.x }
        var minY = 0
        var maxY = points.maxOf { it.y }
        var floorLeft = Point (minX - maxY, maxY + 2)
        var floorRight = Point (maxX + maxY, maxY + 2)

        var transform = 0 - floorLeft.x
        maxX = floorRight.x + transform
        minX = 0

        var grid = createGridFromMinAndMax(minY, floorRight.y, minX, maxX)

        var rockLines = input.split("\n")
            .map {
                it.split(" -> ").map {
                    var pair = it.split(",")
                    Point(pair[0].toInt()+transform, pair[1].toInt())
                }
            }.toMutableList()

        var transformedFloorLeft = Point(floorLeft.x +transform, floorLeft.y)
        var transformedFloorRight = Point(floorRight.x + transform, floorRight.y)
        rockLines.add(listOf(transformedFloorLeft, transformedFloorRight))

        createRocks(rockLines, grid)

        var sandLocation = Point(500+transform, 0)
        return simulateSand(grid, sandLocation)
    }

    val testInput = readInput("Day14_test")
    val output = part1(testInput)
    check(output == 24)

    val outputTwo = part2(testInput)
    check(outputTwo == 93)

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))

}
