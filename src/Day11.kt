fun main() {

    fun applyRule(stone: Long): List<Long> {
        if (stone == 0L) {
            return listOf(1L)
        }
        val stoneString = stone.toString()
        if (stoneString.length % 2 == 0) {
            return listOf(
                stoneString.take(stoneString.length / 2).toLong(),
                stoneString.takeLast(stoneString.length / 2).toLong()
            )
        }

        return listOf(stone * 2024L)
    }

    fun blinkWithMemoization(number: Long, remainingBlinks: Int, cache: MutableMap<Pair<Long, Int>, Long>): Long {
        if (remainingBlinks == 0) {
            return 1
        }

        val cacheEntry = cache[Pair(number, remainingBlinks)]

        if (cacheEntry != null) {
            return cacheEntry
        }

        return applyRule(number).sumOf {
            blinkWithMemoization(it, remainingBlinks - 1, cache)
        }.also { cache[Pair(number, remainingBlinks)] = it }
    }

    fun part1(input: List<String>): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        return input.joinToString("").split(" ").map { it.toLong() }
            .sumOf { blinkWithMemoization(it, 25, cache) }
    }

    fun part2(input: List<String>): Long {
        val cache = mutableMapOf<Pair<Long, Int>, Long>()

        return input.joinToString("").split(" ").map { it.toLong() }
            .sumOf { blinkWithMemoization(it, 75, cache) }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day11_test")
    part1(testInput).println()
//    part2(testInput).println()
    check(part1(testInput) == 55312L)
//    check(part2(testInput) == 81)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
