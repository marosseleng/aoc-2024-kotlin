data class Line(val result: Long, val numbers: List<Long>)

fun main() {
    fun parse(input: List<String>): List<Line> {
        return input.map {
            val splitResult = it.split(": ")
            Line(splitResult[0].toLong(), splitResult[1].split(" ").map { it.toLong() })
        }
    }

//    fun canBeComputed(line: Line): Boolean {
//        println("canBeComputed($line)")
//        val division = if (line.result % line.numbers.last() == 0L) {
//            val remainingNumbers = line.numbers.dropLast(1)
//            val newResult = line.result / line.numbers.last()
//            if (remainingNumbers.isEmpty()) {
//                newResult == 1L
//            } else {
//                canBeComputed(Line(newResult, remainingNumbers))
//            }
//        } else {
//            false
//        }
//        println("line $line can be solved by division: $division")
//        if (division) {
//            return true
//        }
//
//        val remainingNumbers = line.numbers.dropLast(1)
//        val newResult = line.result - line.numbers.last()
//        val subtraction = if (remainingNumbers.isEmpty()) {
//            newResult == 0L
//        } else {
//            canBeComputed(Line(newResult, remainingNumbers))
//        }
//
//        println("line $line can be solved by subtraction: $subtraction")
//        return subtraction
//    }


    fun canBeComputed2(line: Line): Boolean {
        println("canBeComputed2($line)")
        if (line.numbers.size == 1 && line.numbers.first() == line.result) {
            println("Line $line is solved because of same number.")
            return true
        }

        val division = if (line.result % line.numbers.last() == 0L) {
            val remainingNumbers = line.numbers.dropLast(1)
            val newResult = line.result / line.numbers.last()
            if (remainingNumbers.isEmpty()) {
                newResult == 1L
            } else {
                canBeComputed2(Line(newResult, remainingNumbers))
            }
        } else {
            false
        }
        println("line $line can be solved by division: $division")
        if (division) {
            return true
        }

        val remainingNumbers = line.numbers.dropLast(1)
        val newResult = line.result - line.numbers.last()
        val subtraction = if (remainingNumbers.isEmpty()) {
            newResult == 0L
        } else if (newResult < 0) {
            false
        } else {
            canBeComputed2(Line(newResult, remainingNumbers))
        }

        println("line $line can be solved by subtraction: $subtraction")
        if (subtraction) {
            return true
        }

        val concatenation = if (line.numbers.size >= 2) {
            println("inside concatenation if")
            val last = line.numbers.last()
            val lastString = last.toString()
            val resultString = line.result.toString()
            if (resultString.endsWith(lastString)) {
                resultString == lastString ||
                canBeComputed2(Line(
                    result = line.result.toString().dropLast(lastString.length).toLong(),
                    numbers = line.numbers.dropLast(1),
                ))
            } else {
                false
            }
        } else {
            println("inside concatenation if")
            false
        }
        println("line $line can be solved by concatenation: $concatenation")
        return concatenation
    }

//    fun part1(input: List<String>): Long {
//        return parse(input)
//            .filter { canBeComputed(it) }
//            .sumOf {
//                println("in sumOf($it)")
//                it.result
//            }
//    }

    fun part2(input: List<String>): Long {
        return parse(input)
//            .filter { it.result == 7290L }
            .filter { canBeComputed2(it) }
            .sumOf {
                println("in sumOf($it)")
                it.result
            }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day07_test")
//    part1(testInput).println()
//    part2(testInput).println()
//    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day07")
//    part1(input).println()
    part2(input).println()
}
