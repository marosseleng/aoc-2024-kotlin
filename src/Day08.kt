private data class CharCoords(val char: Char, val coords: Set<Coords>)

fun main() {
    fun findAll(input: List<String>, char: Char, forCoords: Coords): Set<CharCoords> {
        return input.flatMapIndexed { y, s ->
            s.mapIndexedNotNull { x, c ->
                if (char == c && x != forCoords.x && y != forCoords.y) {
                    Coords(x, y)
                } else {
                    null
                }
            }
        }.map { otherPoint -> CharCoords(char, setOf(forCoords, otherPoint)) }.toSet()
    }

    fun createPairs(input: List<String>): Set<CharCoords> {
        return input.flatMapIndexed { y, line ->
            line.flatMapIndexed { x, char ->
                if (char.isLetterOrDigit()) {
                    findAll(input, char, Coords(x, y))
                } else {
                    emptySet()
                }
            }
        }.toSet()
    }

    fun isInBounds(point: Coords, width: Int, height: Int): Boolean {
        return point.x in 0 until width && point.y in 0 until height
    }

    fun antinodesOfPoint(point: Coords, xDiff: Int, yDiff: Int, width: Int, height: Int): Set<Coords> {
        val result = mutableSetOf<Coords>()
        var newPoint = point

        while(isInBounds(newPoint, width, height)) {
            result.add(newPoint)
            newPoint = Coords(newPoint.x + xDiff, newPoint.y + yDiff)
        }

        return result
    }

    fun getOppositePoints(first: Coords, second: Coords, width: Int, height: Int, part2: Boolean = false): Set<Coords> {
        val firstNew = Coords(first.x + (first.x - second.x), first.y + (first.y - second.y))
        val secondNew = Coords(second.x + (second.x - first.x), second.y + (second.y - first.y))
        return listOf(firstNew, secondNew).filter {
            it.x in 0 until width && it.y in 0 until height
        }.toSet()
    }

    fun computePoints(pairs: Set<CharCoords>, width: Int, height: Int): Int {
        return pairs.flatMap { cc ->
            val list = cc.coords.toList()
            getOppositePoints(list[0], list[1], width, height)
        }.toSet().count()
    }

    fun computePointsPart2(pairs: Set<CharCoords>, width: Int, height: Int): Int {
        return pairs.flatMap { cc ->
            val list = cc.coords.toList()
            val first = list[0]
            val second = list[1]

            antinodesOfPoint(first, first.x - second.x, first.y - second.y, width, height) +
            antinodesOfPoint(second, second.x - first.x, second.y - first.y, width, height)
        }.toSet().count()
    }

    fun part1(input: List<String>): Int {
        return computePoints(createPairs(input), input.first().length, input.size)
    }

    fun part2(input: List<String>): Int {
        return computePointsPart2(createPairs(input), input.first().length, input.size)
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day08_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
