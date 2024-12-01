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
    input.lineSequence()
        .map { it.split("   ") }
        .map { it[0].toInt() to it[1].toInt() }
        .fold(listOf<Int>() to listOf<Int>()) { (left, right), (id1, id2) -> left + id1 to right + id2 }

class Day01Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(1)).isEqualTo(2113135)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(1)).isEqualTo(19097157)
}
