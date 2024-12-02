import assertk.assertThat
import assertk.assertions.isEqualTo
import util.input
import util.invokedWith
import org.junit.jupiter.api.Test

private fun part1(input: String) = parse(input).count { isSafe(it) }

private fun part2(input: String) = parse(input).count { isSafeWithProblemDampener(it) }

private fun parse(input: String) = input.lineSequence().map { line -> line.splitToSequence(" ").map(String::toInt) }

private fun isSafeWithProblemDampener(levels: Sequence<Int>): Boolean {
    val dampened = levels.mapIndexed { remove, _ -> levels.filterIndexed { i, _ -> i != remove } }
    return isSafe(levels) || dampened.any { isSafe(it) }
}

private fun isSafe(levels: Sequence<Int>): Boolean {
    val diffs = levels.windowed(2).map { (a, b) -> b - a }
    return diffs.all { it in 1..3 } || diffs.all { it in -3..-1 }
}

class Day02Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(2)).isEqualTo(282)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(2)).isEqualTo(349)
}
