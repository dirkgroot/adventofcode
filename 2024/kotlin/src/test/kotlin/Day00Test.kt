import assertk.assertThat
import assertk.assertions.isEqualTo
import util.input
import util.invokedWith
import org.junit.jupiter.api.Test

private fun part1(input: String) = input.length

private fun part2(input: String) = input.length

class Day00Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(0)).isEqualTo(21)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(0)).isEqualTo(21)
}
