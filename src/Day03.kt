fun main() {
    fun part1(input: List<String>): Int {
        val regex = """(mul\(\d{1,3},\d{1,3}\))""".toRegex()
        return regex.findAll(input.joinToString(separator = "")).sumOf { matchResult ->
            matchResult.value
                .drop(4) // drop "mul("
                .dropLast(1) // drop ")"
                .split(",")
                .map { it.toInt() }
                .reduce { acc, i -> acc * i }
        }
    }

    fun part2(input: List<String>): Int {
        val joinedInput = input.joinToString(separator = "")
        val mulRegexResult = """(mul\(\d{1,3},\d{1,3}\))""".toRegex().findAll(joinedInput)
        val doRegexResult = """do\(\)""".toRegex().findAll(joinedInput)
        val dontRegexResult = """don't\(\)""".toRegex().findAll(joinedInput)

        var lastMulEndIndex = 0
        var applyMul = true

        return mulRegexResult.sumOf { matchResult ->
            val value = matchResult.value
                .drop(4) // drop "mul("
                .dropLast(1) // drop ")"
                .split(",")
                .map { it.toInt() }
                .reduce { acc, i -> acc * i }

            val mulStartIndex = matchResult.range.start

            val oldApplyMul = applyMul
            if (oldApplyMul) {
                // check if there isn't don't()
                val previousDont = dontRegexResult.find {
                    it.range.start > lastMulEndIndex && it.range.endInclusive < mulStartIndex
                }
                if (previousDont != null) {
                    applyMul = false
                }
            } else {
                // check if there isn't a new do()
                val previousDo = doRegexResult.find {
                    it.range.start > lastMulEndIndex && it.range.endInclusive < mulStartIndex
                }
                if (previousDo != null) {
                    applyMul = true
                }
            }

            lastMulEndIndex = matchResult.range.endInclusive

            if (applyMul) {
                value
            } else {
                0
            }
        }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day03_test")
//    part1(testInput).println()
//    part2(testInput).println()
    check(part1(testInput) == 161)
    check(part2(listOf("xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
