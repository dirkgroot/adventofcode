import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import util.input
import util.invokedWith
import java.util.*

private fun part2(input: String): Long {
    val c = input.mapIndexed { index, c ->
        val id = if (index % 2 == 0) index / 2 else -1
        Chunk(id, c.digitToInt(), index % 2 != 0)
    }
    val chunks = LinkedList(c)

    val moved = mutableSetOf<Chunk>()
    var i = chunks.lastIndex
    while (i >= 0) {
        val chunk = chunks[i]
        if (!chunk.isFree && chunk !in moved) {
            val free = chunks.indexOfFirst { it.isFree && it.size >= chunk.size }
            if (free in 0..<i) {
                chunks[free].size -= chunk.size
                chunks.removeAt(i)
                chunks.add(free, chunk)
                chunks.add(i, Chunk(-1, chunk.size, true))
                moved.add(chunk)
            }
        }
        i--
    }

    val disk = chunks.asSequence()
        .flatMap { chunk -> generateSequence { chunk.id }.take(chunk.size) }
        .toList().toTypedArray()

    return disk.withIndex()
        .filter { (_, chunkID) -> chunkID >= 0 }
        .sumOf { (index, chunkID) -> (index * chunkID).toLong() }
}

private data class Chunk(val id: Int, var size: Int, val isFree: Boolean)

class Day09Test {
    @Test
    fun `part2 example`() = assertThat(::part2 invokedWith "2333133121414131402").isEqualTo(2858L)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(9)).isEqualTo(6359491814941L)
}
