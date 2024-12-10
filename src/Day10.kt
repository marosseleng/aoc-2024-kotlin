private data class TopoPoint(
    val height: Int,
    val reachableTops: Set<Coords>,
    val count: Int
)
fun main() {

    fun getSurroundingPoints(coords: Coords): List<Coords> {
        return listOf(
            Coords(coords.x - 1, coords.y),
            Coords(coords.x + 1, coords.y),
            Coords(coords.x, coords.y - 1),
            Coords(coords.x, coords.y + 1),
        )
    }

    fun fillInTheMap(input: List<String>): Map<Coords, TopoPoint> {
        val map = mutableMapOf<Coords, TopoPoint>()

        val mapHeight = input.size
        val mapWidth = input[0].length

        input.forEachIndexed { y, line ->
            line.forEachIndexed { x, c ->
                val height = c.digitToInt()
                val reachableTops = if (height == 9) {
                    setOf(Coords(x, y))
                } else {
                    emptySet()
                }
                val count = if (height == 9) {
                    1
                } else {
                    0
                }
                map[Coords(x, y)] = TopoPoint(height, reachableTops, count)
            }
        }

        for (height in 9 downTo 0) {
            map.filterValues { it.height == height }.keys.forEach { outerCoords ->
                val outerPointReachableTops = map[outerCoords]?.reachableTops ?: emptySet()
                val outerPointCount = map[outerCoords]?.count ?: 0
                if (outerPointReachableTops.isEmpty() || outerPointCount == 0) {
                    return@forEach
                }
                getSurroundingPoints(outerCoords)
                    .filter { isInBounds(it, mapWidth, mapHeight) }
                    .forEach inner@{ coords ->
                        val point = map[coords] ?: return@inner
                        if (point.height + 1 != height) {
                            return@inner
                        }
                        map[coords] = point.copy(
                            reachableTops = point.reachableTops + outerPointReachableTops,
                            count = point.count + outerPointCount,
                        )
                    }
            }
        }
        return map
    }

    fun part1(input: List<String>): Int {
        return fillInTheMap(input)
            .filterValues { it.height == 0 }
            .values
            .sumOf { it.reachableTops.size }
    }

    fun part2(input: List<String>): Int {
        return fillInTheMap(input)
            .filterValues { it.height == 0 }
            .values
            .sumOf { it.count }
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day10_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
