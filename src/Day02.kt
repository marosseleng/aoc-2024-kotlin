import kotlin.math.abs

fun main() {
    fun parse(input: List<String>): List<List<Int>> {
        return input.map { it.split(" ").map { it.toInt() } }
    }

    fun isSafePart1(row: List<Int>): Boolean {
//        println("testing row $row")
        var increasing: Boolean? = null
        return row.reduce { acc, i ->
            val difference = i - acc
            if (abs(difference) in 1..3) {
                when (increasing) {
                    null -> increasing = (difference > 0)/*.also { println("setting increasing=$it") }*/
                    true -> if (difference < 0) {
//                        println("Comparing $acc and $i -> FAILED: increasing")
                        return false
                    }
                    false -> if (difference > 0) {
//                        println("Comparing $acc and $i -> FAILED: decreasing")
                        return false
                    }
                }
                i
            } else {
//                println("Difference between $acc and $i is out of bounds.")
                return false
            }
        } == row.last()
    }

    fun part1(input: List<String>): Int {
        return parse(input).count { row ->
            isSafePart1(row)
        }
    }

    fun part2(input: List<String>): Int {
        return parse(input).count { row ->
            if (isSafePart1(row)) {
                true
            } else {
                row.indices.any { index ->
                    val withDeleted = row.toMutableList().also { it.removeAt(index) }
                    isSafePart1(withDeleted)
                }
            }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day02_test")
//    part1(testInput).println()
//    part2(testInput).println()
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
