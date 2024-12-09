fun main() {

    fun parse(input: List<String>): List<Long> {
        val allLines = input.joinToString("")

        var currentIndex = 0L
        var wasData = false

        return allLines.flatMap { c ->
            val result = mutableListOf<Long>()
            val long = c.toString().toLong()
            if (!wasData) {
                (0L until long).forEach { i -> result.add(currentIndex) }
                currentIndex++
            } else {
                (0L until long).forEach { i -> result.add(-1L) }
            }
            wasData = !wasData
            result
        }
    }

    fun isValid(blocks: List<Long>): Boolean {
        return blocks.indexOfFirst { it < 0 } > blocks.indexOfLast { it >= 0 }
    }

    fun moveOneBlock(blocks: List<Long>): List<Long> {
        val result = blocks.toMutableList()

        val lastData = blocks.indexOfLast { it >= 0L }
        val firstEmpty = blocks.indexOfFirst { it < 0L }

        val removedData = result.removeAt(lastData)

        result.add(lastData, -1L)
        result.removeAt(firstEmpty)
        result.add(firstEmpty, removedData)
        return result
    }

    fun findEmptyBlock(blocks: List<Long>, minSize: Int): IntRange? {
        var startIndex = -1
        var endIndex = -1

        blocks.forEachIndexed { index, block ->
            if (block >= 0) {
                if (startIndex != -1 && endIndex - startIndex + 1 >= minSize) {
                    return startIndex..endIndex
                }
                // we found file, reset counters
                startIndex = -1
                endIndex = -1
                return@forEachIndexed
            }
            if (startIndex == -1) {
                startIndex = index
                endIndex = index
            } else {
                endIndex = index
            }
        }

        return null
    }

    fun findFile(blocks: List<Long>, until: Long): IntRange? {
        if (until == 0L) {
            return null
        }
        val endIndex = blocks.indexOfLast { it in 0..<until }
        val foundNumber = blocks[endIndex]
        var startIndex = endIndex

        if (endIndex == 0) {
            return 0..0
        }

        for (i in endIndex - 1 downTo 0) {
            if (blocks[i] >= 0 && blocks[i] == foundNumber) {
                startIndex = i
            } else {
                break
            }
        }

        if (startIndex == 0) {
            // we are out of files
            return null
        }

        return startIndex..endIndex
    }

    fun replace(blocks: List<Long>, emptyRange: IntRange, fileRange: IntRange): List<Long> {
        if (fileRange.first <= emptyRange.first) {
            return blocks
        }

        val mutableBlocks = blocks.toMutableList()

        fileRange.withIndex().forEach {
            mutableBlocks[emptyRange.start + it.index] = mutableBlocks[it.value]
            mutableBlocks[it.value] = -1L
        }

        return mutableBlocks
    }

    fun moveOneFile(blocks: List<Long>, maxNumber: Long): Pair<List<Long>, Long> {
        val fileBlock = findFile(blocks, maxNumber)
        if (fileBlock == null) {
            // no files
//            println("moveOneFile(maxNum=$maxNumber):\n$blocks\n\n\n\n=>no more files")
            return blocks to Long.MIN_VALUE
        }
        val emptyBlock = findEmptyBlock(blocks, fileBlock.count())
        if (emptyBlock == null) {
            // no free space for this file
//            println("moveOneFile(maxNum=$maxNumber):\n$blocks\n\n$fileBlock\n=>no more empty blocks")
            return blocks to blocks[fileBlock.first]
        }

        return (replace(blocks, emptyBlock, fileBlock) to blocks[fileBlock.first]).also {
//            println("moveOneFile(maxNum=$maxNumber):\n$blocks\n$emptyBlock\n$fileBlock\n=>${it.first}")
        }
    }

    fun calculateChecksum(blocks: List<Long>): Long {
        return blocks.dropWhile { it < 0 }
            .mapIndexed { index, l -> index * l }
            .sum()
    }

    fun calculateChecksum2(blocks: List<Long>): Long {
        return blocks
            .mapIndexed { index, l ->
                if (l >= 0) {
                    index * l
                } else {
                    0
                }
            }
            .sum()
    }

    fun part1(input: List<String>): Long {
        var parsed = parse(input)

        while (!isValid(parsed)) {
            parsed = moveOneBlock(parsed)
        }

        return calculateChecksum(parsed)
    }

    fun part2(input: List<String>): Long {
        var parsed = parse(input)
        var max = Long.MAX_VALUE
        while (max > Long.MIN_VALUE) {
            val (newParsed, newMax) = moveOneFile(parsed, max)
            parsed = newParsed
            max = newMax
        }

        return calculateChecksum2(parsed)
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day09_test")
//    part1(testInput).println()
    part2(testInput).println()
//    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day09")
//    part1(input).println()
    part2(input).println()
}
