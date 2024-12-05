fun main() {
    fun parse(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        return input
            .takeWhile { it.isNotEmpty() }
            .map {
                val splitResult = it.split('|').map { it.toInt() }
                splitResult[0] to splitResult[1]
            } to input.dropWhile { it.isNotEmpty() }.drop(1).map { it.split(",").map { it.toInt() } }
    }

    fun part1(input: List<String>): Int {
        val (rules, values) = parse(input)
        val correctValues = values.filter { vals ->
            val applicableRules = rules.filter { it.first in vals && it.second in vals }

            applicableRules.all { rule ->
                vals.indexOf(rule.first) < vals.indexOf(rule.second)
            }
        }
        return correctValues.sumOf {
            it[it.size / 2]
        }
    }

    fun fixValues(values: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        return values.sortedByDescending { value ->
            rules.count { it.first == value }
        }
    }

    fun part2(input: List<String>): Int {
        val (rules, values) = parse(input)
        val incorrectValues = values.filterNot { vals ->
            val applicableRules = rules.filter { it.first in vals && it.second in vals }

            applicableRules.all { rule ->
                vals.indexOf(rule.first) < vals.indexOf(rule.second)
            }
        }
        return incorrectValues
            .map { vals ->
                fixValues(vals, rules.filter { it.first in vals && it.second in vals })
            }
            .sumOf { it[it.size / 2] }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day05_test")
//    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
