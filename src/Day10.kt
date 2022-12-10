fun main() {

    class Register() {
        var x = 1
        var cycles = 1
        var signalStrength = 0

        fun addx(v: Int) {
            // 2 cycles
            this.checkSignalStrength()
            cycles++
            this.checkSignalStrength()
            cycles++
            x = x + v
        }

        fun noop() {
            this.checkSignalStrength()
            cycles++
        }

        fun checkSignalStrength() {
            if (cycles == 20 || (cycles-20) % 40 == 0) {
                signalStrength = signalStrength + (x*cycles)
            }
        }
    }

    class PrintRegister() {
        var x = 1
        var cycles = 0

        fun addx(v: Int) {
            // 2 cycles
            this.printPixel()
            cycles++
            this.printPixel()
            cycles++
            x = x + v
        }

        fun noop() {
            this.printPixel()
            cycles++
        }

        fun printPixel() {
            if (Math.abs(cycles%40 - x) <= 1) {
                print('#')
            } else {
                print('.')
            }
            if ((cycles+1) % 40 == 0) {
                println()
            }
        }
    }

    fun part1(input: String): Int {
        val register = Register()
        input.split("\n")
            .forEach {
                var instruction = it.split(" ")
                if (instruction[0] == "noop") {
                    register.noop()
                } else {
                    register.addx(instruction[1].toInt())
                }
            }
        return register.signalStrength
    }

    fun part2(input: String) {
        val register = PrintRegister()
        input.split("\n")
            .forEach {
                var instruction = it.split(" ")
                if (instruction[0] == "noop") {
                    register.noop()
                } else {
                    register.addx(instruction[1].toInt())
                }
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    val output = part1(testInput)
    check(output == 13140)

    part2(testInput)
    println(" ")


    val input = readInput("Day10")
    println(part1(input))
    part2(input)

}
