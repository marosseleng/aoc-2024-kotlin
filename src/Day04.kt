fun main() {
    val XMAS = "XMAS".toRegex()
    val SMAX = "SAMX".toRegex()
    fun searchHorizontally(input: List<String>): Int {
        return input.sumOf { line ->
            XMAS.findAll(line).count() + SMAX.findAll(line).count()
        }
    }

    fun transpose(input: List<String>): List<String> {
        return input.first().indices.map { index ->
            input.map { it[index] }.joinToString("")
        }
    }

    fun searchVertically(input: List<String>): Int {
        return searchHorizontally(transpose(input))
    }

    fun searchDiagonallyCoordinate(input: List<String>, pattern: String, x: Int, y: Int, xDiff: Int, yDiff: Int): Int {
        if (pattern.isEmpty()) {
            return 1
        }
        if (x > input.first().lastIndex || x < 0 || y > input.lastIndex || y < 0) {
            return 0
        }
        val char = input[y][x]
        return if (char == pattern.first()) {
            // letter at coordinates match, go further
            searchDiagonallyCoordinate(input, pattern.drop(1), x + xDiff, y + yDiff, xDiff, yDiff)
        } else {
            0
        }
    }

    fun searchDiagonally(input: List<String>): Int {
        return input.mapIndexed { y, line ->
            line.mapIndexed { x, char ->
                if (char == 'X') {
                    searchDiagonallyCoordinate(input, "MAS", x-1, y-1, -1, -1) +
                            searchDiagonallyCoordinate(input, "MAS", x-1, y+1, -1, 1) +
                            searchDiagonallyCoordinate(input, "MAS", x+1, y-1, 1, -1) +
                            searchDiagonallyCoordinate(input, "MAS", x+1, y+1, 1, 1)
                } else {
                    0
                }
            }.sum()
        }.sum()
    }

    fun part1(input: List<String>): Int {
        return searchHorizontally(input) + searchVertically(input) + searchDiagonally(input)
    }

    fun part2(input: List<String>): Int {
        return input.mapIndexed { y, line ->
            if (y == 0 || y == input.lastIndex) {
                0
            } else {
                line.mapIndexed { x, char ->
                    when {
                        x == 0 || x == line.lastIndex -> 0
                        char != 'A' -> 0
                        else -> {
                            println("Found A in coords [$x][$y]")
                            when {
                                input[y-1][x-1] == 'M' && input[y-1][x+1] == 'S' -> {
                                    println("Got X at [${x-1}][${y-1}] and S at [${x+1}][${y-1}]")
                                    if (input[y+1][x+1] == 'S' && input[y+1][x-1] == 'M') {
                                        1
                                    } else {
                                        0
                                    }
                                }
                                input[y-1][x-1] == 'S' && input[y-1][x+1] == 'M' -> {
                                    println("Got S at [${x-1}][${y-1}] and X at [${x+1}][${y-1}]")
                                    if (input[y+1][x+1] == 'M' && input[y+1][x-1] == 'S') {
                                        1
                                    } else {
                                        0
                                    }
                                }
                                input[y-1][x-1] == 'S' && input[y-1][x+1] == 'S' -> {
                                    println("Got S at [${x-1}][${y-1}] and X at [${x+1}][${y-1}]")
                                    if (input[y+1][x+1] == 'M' && input[y+1][x-1] == 'M') {
                                        1
                                    } else {
                                        0
                                    }
                                }
                                input[y-1][x-1] == 'M' && input[y-1][x+1] == 'M' -> {
                                    println("Got S at [${x-1}][${y-1}] and X at [${x+1}][${y-1}]")
                                    if (input[y+1][x+1] == 'S' && input[y+1][x-1] == 'S') {
                                        1
                                    } else {
                                        0
                                    }
                                }
                                else -> 0
                            }
                        }
                    }
                }.sum()
            }
        }.sum()
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    val testInput = readInput("Day04_test")
//    part1(testInput).println()
    part2(testInput).println()
//    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
