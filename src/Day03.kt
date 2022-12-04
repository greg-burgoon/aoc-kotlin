
import kotlin.collections.HashMap

fun main() {

    var priorityValues = HashMap<Char, Int>()

    var count = 1
    for(c in 'a'..'z') {
        priorityValues.put(c, count)
        count++
    }

    for(c in 'A'..'Z') {
        priorityValues.put(c, count)
        count++
    }

    fun part1(input: String): Int {
        return input.split("\n")
            .map {
                var setOne = HashSet<Char>()
                var setTwo = HashSet<Char>()
                it.forEachIndexed { index, c ->  if (index < it.count()/2) setOne.add(c) else setTwo.add(c)}
                setOne.retainAll(setTwo)
                setOne.first()
            }
            .sumOf { c: Char -> priorityValues.getOrDefault(c, 0) }
    }

    fun part2(input: String): Int {
        return input.split("\n")
            .mapIndexed { index, s ->  if ((index + 1) % 3 == 0) s + "\n" else s }
            .joinToString("\n")
            .split("\n\n")
            .map {

                var setOne = HashSet<Char>()
                var setTwo = HashSet<Char>()
                var setThree = HashSet<Char>()
                it
                    .split("\n")
                    .forEachIndexed { index, s ->
                        if (index == 0) s.forEach { c ->  setOne.add(c)}
                        else if (index == 1) s.forEach { c ->  setTwo.add(c)}
                        else if (index == 2) s.forEach { c ->  setThree.add(c)}
                    }
                setOne.retainAll(setTwo)
                setOne.retainAll(setThree)
                setOne.first()
            }
            .sumOf { c: Char -> priorityValues.getOrDefault(c, 0) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    val output = part1(testInput)
    check(output== 157)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
