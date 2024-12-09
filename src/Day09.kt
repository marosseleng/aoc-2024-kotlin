private sealed interface Block {
    val blocksCount: ULong
}

private data class Data(val id: ULong, override val blocksCount: ULong) : Block {
    override fun toString(): String {
        return buildString { repeat((0UL until blocksCount).count()) { append("$id") } }
    }
}

private data class Empty(override val blocksCount: ULong) : Block {
    override fun toString(): String {
        return buildString { repeat((0UL until blocksCount).count()) { append(".") } }
    }
}

fun main() {

    fun parse2(input: List<String>): List<Long> {
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
//    fun parse(input: List<String>): List<Block> {
//        val allLines = input.joinToString("")
//
//        var currentIndex = 0UL
//        var wasData = false
//
//        val result = mutableListOf<Block>()
//        allLines.forEach { char ->
//            if (!wasData) {
//                result.add(Data(currentIndex++, char.toString().toULong()))
//            } else {
//                result.add(Empty(char.toString().toULong()))
//            }
//            wasData = !wasData
//        }
//
//        return result.also { println("list size=${it.size}") }
//    }

//    fun isValid(blocks: List<Block>): Boolean {
//        return blocks.indexOfFirst { it is Empty } > blocks.indexOfLast { it is Data }
//    }

    fun isValid2(blocks: List<Long>): Boolean {
        return blocks.indexOfFirst { it < 0 } > blocks.indexOfLast { it >= 0 }
    }

//    fun mergeNeighboringEmpty(input: List<Block>): List<Block> {
//        if (input.isEmpty()) return emptyList()
//
//        val result = mutableListOf<Block>()
//        var current = input.first()
//
//        for (i in 1 until input.size) {
//            val next = input[i]
//            if (current is Empty && next is Empty) {
//                println("Merging")
//                current = current.copy(blocksCount = current.blocksCount + next.blocksCount)
//            } else {
//                result.add(current)
//                current = next
//            }
//        }
//        result.add(current) // Add the last item
//        return result
//    }

    fun moveOneBlock2(blocks: List<Long>): List<Long> {
        val result = blocks.toMutableList()

        val lastData = blocks.indexOfLast { it >= 0L }
        val firstEmpty = blocks.indexOfFirst { it < 0L }

        val removedData = result.removeAt(lastData)

        result.add(lastData, -1L)
        result.removeAt(firstEmpty)
        result.add(firstEmpty, removedData)
        return result
    }

//    fun moveOneBlock(blocks: List<Block>): List<Block> {
//        val mutableBlocks = blocks.toMutableList()
//        val indexOfLastData = mutableBlocks.indexOfLast { it is Data }
//        val last = mutableBlocks.removeAt(indexOfLastData)
//        if (last !is Data) {
//            throw RuntimeException("last isn't data.")
//        }
//        val newBlockCount = last.blocksCount - 1UL
//
//        // add 1 empty to be after (possibly) added original
//        mutableBlocks.add(indexOfLastData, Empty(1UL))
//        if (newBlockCount > 0UL) {
//            // we need to add it back
//            mutableBlocks.add(indexOfLastData, last.copy(blocksCount = newBlockCount))
//        }
//
//        val indexOfFirstEmpty = mutableBlocks.indexOfFirst { it is Empty }
//
//        val firstEmpty = mutableBlocks.removeAt(indexOfFirstEmpty)
//        if (firstEmpty !is Empty) {
//            throw RuntimeException("first isn't empty.")
//        }
//
//        val remainingEmpty = firstEmpty.blocksCount - 1UL
//
//        if (remainingEmpty != 0UL) {
//            mutableBlocks.add(indexOfFirstEmpty, Empty(remainingEmpty))
//        }
//
//        val previousDataIndex = indexOfFirstEmpty - 1
//        val previousDataTmp = mutableBlocks[previousDataIndex]
//        if ((previousDataTmp as? Data)?.id == last.id) {
//            val newData =
//                (mutableBlocks.removeAt(previousDataIndex) as Data).copy(blocksCount = previousDataTmp.blocksCount + 1UL)
//            mutableBlocks.add(previousDataIndex, newData)
//        } else {
//            mutableBlocks.add(indexOfFirstEmpty, Data(id = last.id, blocksCount = 1UL))
//        }
//        return mutableBlocks
//    }


//    fun calculateChecksum(blocks: List<Block>): ULong {
//        return blocks.filterIsInstance<Data>()
//            .flatMap { block ->
//                (0UL until block.blocksCount).map { block.id }
//            }
//            .mapIndexed { index, i -> index.toULong() * i }
//            .sumOf { it }
//    }

    fun calculateChecksum2(blocks: List<Long>): Long {
        return blocks.takeWhile { it >= 0 }
            .mapIndexed { index, l -> index * l }
            .sum()
    }

    fun part1(input: List<String>): Long {
        var parsed = parse2(input)

        while (!isValid2(parsed)) {
            parsed = moveOneBlock2(parsed)
        }

        return calculateChecksum2(parsed)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // Or read a large test input from the `src/Day01_test.txt` file:
    println("**Test Data**")
    val testInput = readInput("Day09_test")
    part1(testInput).println()
//    part2(testInput).println()
    check(part1(testInput) == 1928L)
//    check(part2(testInput) == 34)
    println("**Real Data**")

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day09")
    part1(input).println()
//    part2(input).println()
}
