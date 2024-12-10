data class TheMap(
    val obstacles: Set<Coords>,
    val startPosition: VisitedPosition,
    val width: Int,
    val height: Int,
)

data class VisitedPosition(val coords: Coords, val direction: Char)

fun main() {
    val directions = setOf('^','v','>','<')
    fun parseInput(input: List<String>): TheMap {
        var startPosition = Coords(0, 0)
        var direction = '-'
        val obstacles = mutableSetOf<Coords>()

        input.forEachIndexed { y, line ->
            line.forEachIndexed charLoop@{ x, c ->
                if (c in directions) {
                    startPosition = Coords(x, y)
                    direction = c
                } else if (c == '#') {
                    obstacles.add(Coords(x, y))
                }
            }
        }

        return TheMap(
            obstacles = obstacles,
            startPosition = VisitedPosition(startPosition, direction = direction),
            width = input.first().length,
            height = input.size,
        )
    }

    fun isLeavingTheMap(position: VisitedPosition, map: TheMap): Boolean {
        return when (position.direction) {
            '^' -> position.coords.y == 0
            'v' -> position.coords.y == map.height - 1
            '<' -> position.coords.x == 0
            '>' -> position.coords.x == map.width - 1
            else -> false
        }
    }

    fun getNewPosition(current: VisitedPosition): Coords {
        return when (current.direction) {
            '^' -> Coords(current.coords.x, current.coords.y - 1)
            'v' -> Coords(current.coords.x, current.coords.y + 1)
            '<' -> Coords(current.coords.x - 1, current.coords.y)
            '>' -> Coords(current.coords.x + 1, current.coords.y)
            else -> throw RuntimeException("Unknown direction ${current.direction}")
        }
    }

    fun move(position: VisitedPosition, map: TheMap): VisitedPosition {
        val newPosition = getNewPosition(position)

        return if (newPosition in map.obstacles) {
            val newDirection = when (position.direction) {
                '^' -> '>'
                'v' -> '<'
                '<' -> '^'
                '>' -> 'v'
                else -> throw RuntimeException("Unknown direction ${position.direction}")
            }
            VisitedPosition(Coords(position.coords.x, position.coords.y), newDirection)
        } else {
            VisitedPosition(newPosition, position.direction)
        }
    }

    // ^ v > <
    fun part1(input: List<String>): Int {
        val map = parseInput(input)
        val visited = mutableSetOf<VisitedPosition>()
        var currentGuardPosition = map.startPosition
        visited.add(map.startPosition)

        do {
            val newPosition = move(currentGuardPosition, map)
            visited.add(newPosition)
            currentGuardPosition = newPosition
        } while (!isLeavingTheMap(currentGuardPosition, map))

        return visited.distinctBy { it.coords }.count()
    }

    fun willLeaveMap(alreadyVisited: Set<VisitedPosition>, currentPosition: VisitedPosition, map: TheMap): Boolean {
        val visited = alreadyVisited.toMutableSet()
        var currentGuardPosition = currentPosition
        visited.add(currentGuardPosition)
        do {
            val newPosition = move(currentGuardPosition, map)
            if (!visited.add(newPosition)) {
                // This element is already in the map. We found a circle
                return false
            }

            currentGuardPosition = newPosition
        } while (!isLeavingTheMap(currentGuardPosition, map))
        return true
    }

    fun part2(input: List<String>): Int {
        val alreadyTried = mutableSetOf<Coords>()

        val map = parseInput(input)
        val visited = mutableSetOf<VisitedPosition>()
        var currentGuardPosition = map.startPosition
        visited.add(map.startPosition)

        do {
            val nextObstacle = getNewPosition(currentGuardPosition)
            if (nextObstacle !in visited.map { it.coords }) {
                // we can't place an obstacle to where we already been.
                val mapCopy = map.copy(obstacles = map.obstacles.toMutableSet().also { it.add(nextObstacle) })
                if (!willLeaveMap(visited, currentGuardPosition, mapCopy)) {
                    alreadyTried.add(nextObstacle)
                }
            }
            val newPosition = move(currentGuardPosition, map)
            visited.add(newPosition)
            currentGuardPosition = newPosition
        } while (!isLeavingTheMap(currentGuardPosition, map))
        return (alreadyTried - map.obstacles - map.startPosition.coords).size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day06_test")
    part1(testInput).println()
    part2(testInput).println()
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
