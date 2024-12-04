import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import util.input
import util.invokedWith

private fun part1(input: String) =
    """mul\((\d+),(\d+)\)""".toRegex().findAll(input)
        .sumOf { it.groups[1]!!.value.toInt() * it.groups[2]!!.value.toInt() }

private fun part2(input: String) =
    parse(input)
        .fold(true to 0) { (doing, sum), instruction ->
            when (instruction) {
                is Dont -> false to sum
                is Do -> true to sum
                is Multiply -> if (doing) true to sum + (instruction.a * instruction.b) else false to sum
            }
        }
        .let { (_, sum) -> sum }

private fun parse(input: String) =
    """do\(\)|don't\(\)|mul\((\d+),(\d+)\)""".toRegex().findAll(input)
        .map {
            when {
                it.value.startsWith("don't") -> Dont
                it.value.startsWith("do") -> Do
                else -> Multiply(it.groups[1]!!.value.toInt(), it.groups[2]!!.value.toInt())
            }
        }

private sealed interface Instruction
private data object Do : Instruction
private data object Dont : Instruction
private data class Multiply(val a: Int, val b: Int) : Instruction

class Day03Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(3)).isEqualTo(188116424)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(3)).isEqualTo(104245808)
}