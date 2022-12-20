import kotlin.math.pow

fun main() {
    data class Rock(val width: Int, val height: Int, val boundingBox: List<Int>)

    data class GameTower(val width: Int, val rockStartX: Int, var grid: MutableList<Int>, var currentFillHeight: Long)

    data class FallingRock(var rock: Rock, var gameTower: GameTower) {
        var currentXPosition = gameTower.rockStartX
        var currentYPosition = gameTower.grid.size-1

        fun hasCollision(x: Int, y: Int): Boolean {
            var hasCollision = false
            if (x < 0 || y < rock.boundingBox.size-1 || x+rock.width > gameTower.width) {
                return true
            } else {
                rock.boundingBox.forEachIndexed { index, rockRow ->
                    hasCollision = hasCollision || (((rockRow shl (gameTower.width-rock.width)) shr x) and gameTower.grid.get(y-index) > 0L)
                }
            }
            return hasCollision
        }
        fun moveLeft(): Boolean {
            return !hasCollision(currentXPosition-1, currentYPosition).also { hasCollision ->
                    if (!hasCollision) {
                        currentXPosition = currentXPosition-1
                    }
            }
        }

        fun moveRight(): Boolean {
            return !hasCollision(currentXPosition+1, currentYPosition).also { hasCollision ->
                if (!hasCollision) {
                    currentXPosition = currentXPosition+1
                }
            }
        }

        fun moveDown(): Boolean {
            return !hasCollision(currentXPosition, currentYPosition-1).also { hasCollision ->
                if (!hasCollision) {
                    currentYPosition = currentYPosition-1
                }
            }
        }

    }
    data class GameState(var rocksFallen: Long, var currentFallingRock: FallingRock?, var arrows: ArrayDeque<Char>, var rockList: ArrayDeque<Rock>)

    fun createRocks(): List<Rock>{
        val dashRock = Rock(4, 1, listOf<Int>((2.0.pow(4.0) - 1).toInt()))
        val plusRock = Rock(
            3, 3, listOf<Int>(
                2,
                (2.0.pow(3.0) - 1).toInt(),
                2
            )
        )

        val lRock = Rock(
            3, 3, listOf<Int>(
                1,
                1,
                (2.0.pow(3.0) - 1).toInt()
            )
        )

        val lineRock = Rock(
            1, 4, listOf<Int>(
                1,
                1,
                1,
                1
            )
        )

        val squareRock = Rock(
            2, 2, listOf<Int>(
                3,
                3,
            )
        )

        return listOf<Rock>(
            dashRock,
            plusRock,
            lRock,
            lineRock,
            squareRock
        )
    }

    fun runSimulation(input: String, rocksToFall: Long): Long {
        val rockList = createRocks()

        var gameBoard = GameTower(
            7, 2, mutableListOf(
                0,
                0,
                0,
                0
            ), 0
        )

        var inputs = ArrayDeque<Char>()
        inputs.addAll(input.toCharArray().toList())

        var rockInputs = ArrayDeque<Rock>()
        rockInputs.addAll(rockList)

        var gameState = GameState(0, null, inputs, rockInputs)

        var cycleHeight = 66L
        var calculatedHeight = cycleHeight * (rocksToFall  / (input.length))
        var calculatedDiscountFactor = ((rocksToFall / (input.length) - 1) / 7 * (38))
        var cycleCount = 0L
        var discountFactor = 0L
        var firstRock = gameState.rockList.first()
        while (gameState.rocksFallen < rocksToFall) {
            if (gameState.currentFallingRock == null) {
                var nextRock = gameState.rockList.removeFirst()
//                if (nextRock == firstRock && gameState.rocksFallen > 0 && (gameState.rocksFallen % (input.length) == 0L)) {
//                    var calcHeight = cycleHeight * (gameState.rocksFallen  / (input.length))
//                    cycleCount++
//                    if ((cycleCount - 1 != 0L) && (cycleCount - 1) % 7 == 0L) {
//                        discountFactor = ((cycleCount - 1) / 7 * (38))
//                        gameState.rocksFallen = gameState.rocksFallen + ((gameState.rocksFallen-input.length) * ((rocksToFall / (input.length) - 1))
//                        gameBoard = GameTower(
//                            7, 2, mutableListOf(
//                                0,
//                                0,
//                                0,
//                                0
//                            ), 0
//                        )
//                    }
//
//
//                    print("Cycle: "+ cycleCount)
//                    print("Rocks Fallen: "+ gameState.rocksFallen)
//                    print(", discount: "+ discountFactor)
//                    print(", total discount: "+ calculatedDiscountFactor)
//                    print(", CalculatedHeight :" + calcHeight)
//                    print(", currentHeight :" + gameBoard.grid.size)
//                    println(", Gap:" + (calcHeight - gameBoard.grid.size))
//                }
                gameState.rockList.add(nextRock)
                //check and expand gameBoard Height
                while (gameBoard.grid.takeLast(3 + nextRock.height).sum() != 0) {
                    gameBoard.grid.add(0)
                }
                var currentRock = FallingRock(nextRock, gameBoard)
                gameState.currentFallingRock = currentRock
            } else {
                //check move
                var nextMove = gameState.arrows.removeFirst()
                gameState.arrows.add(nextMove)

                if (nextMove == '>') {
                    gameState.currentFallingRock!!.moveRight()
                }

                if (nextMove == '<') {
                    gameState.currentFallingRock!!.moveLeft()
                }

                if (!gameState.currentFallingRock!!.moveDown()) {
                    //store in grid
                    gameState.currentFallingRock!!.rock.boundingBox.forEachIndexed { index, rockRow ->
                        gameBoard.grid.set(
                            gameState.currentFallingRock!!.currentYPosition - index,
                            (rockRow shl (gameBoard.width - gameState.currentFallingRock!!.rock.width) shr gameState.currentFallingRock!!.currentXPosition) or gameBoard.grid.get(
                                gameState.currentFallingRock!!.currentYPosition - index
                            )
                        )
                    }
                    gameState.currentFallingRock = null
                    gameState.rocksFallen++
                    gameBoard.grid.removeIf({ it == 0 })
                }
            }
        }

        return gameBoard.grid.size.toLong()
    }

    fun part1(input: String): Long {
        return runSimulation(input, 2022L)
    }


    fun part2(input: String): Long {
        return runSimulation(input, 1000000000000L)
    }

    val testInput = readInput("Day17_test")
    val output = part1(testInput)
    check(output == 3068L)

    val outputTwo = part2(testInput)
    check(outputTwo == 1514285714288L)

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))

}
