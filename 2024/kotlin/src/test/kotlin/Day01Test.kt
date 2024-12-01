import assertk.assertThat
import assertk.assertions.isEqualTo
import util.input
import util.invokedWith
import org.junit.jupiter.api.Test
import kotlin.math.abs

private fun part1(input: String) =
    parse(input).let { (left, right) ->
        left.sorted().zip(right.sorted())
            .sumOf { (id1, id2) -> abs(id1 - id2) }
    }

private fun part2(input: String) =
    parse(input).let { (left, right) ->
        left.sumOf { id1 -> id1 * right.count { id2 -> id2 == id1 } }
    }

private fun parse(input: String) =
    input.splitToSequence("   ", "\n")
        .map { it.toInt() }
        .chunked(2).map { (a, b) -> a to b }
        .unzip()

class Day01Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(1)).isEqualTo(2113135)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(1)).isEqualTo(19097157)
}
