import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): Pair<List<Int>, List<Int>> {
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()
        input.forEach { line ->
            val splitResult = line.split("""\s+""".toRegex())
            val left = splitResult.first().toInt()
            val right = splitResult.last().toInt()
            leftList.add(left)
            rightList.add(right)
        }
        return leftList.sorted() to rightList.sorted()
    }
    fun part1(input: List<String>): Int {
        val (leftList, rightList) = parse(input)
        return leftList.zip(rightList).fold(0) { acc, (l, r) ->
            acc + abs(l - r)
        }
    }

    fun part2(input: List<String>): Int {
        val (left, right) = parse(input)

        return left.fold(0) { acc, i ->
            acc + i * (right.count { it == i })
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11)
    check(part2(testInput) == 31)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
