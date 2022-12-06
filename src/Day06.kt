fun main() {

    fun part1(input: String): Int {
        var firstHeader = -1
        input.windowed(4, 1)
            .forEachIndexed { index, s ->
                if (s.length == s.toSet().size  && firstHeader == -1)
                    firstHeader = index + s.length
            }
       return firstHeader
    }

    fun part2(input: String): Int {
        var firstHeader = -1
        input.windowed(14, 1)
            .forEachIndexed { index, s ->
                if (s.length == s.toSet().size  && firstHeader == -1)
                    firstHeader = index + s.length
            }
        return firstHeader
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    val output = part1(testInput)
    check(output == 11)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}
