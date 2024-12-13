import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import util.input
import util.invokedWith

private fun part1(input: String): Long =
    parse(input).sumOf { it.cost }

private fun part2(input: String) =
    parse(input)
        .map { it.copy(prizeX = it.prizeX + 10000000000000L, prizeY = it.prizeY + 10000000000000) }
        .sumOf { it.cost }

private fun parse(input: String) =
    input.split("\n\n")
        .map { machine ->
            val lines = machine.lines()
            val buttonRegex = "Button .: X\\+(\\d+), Y\\+(\\d+)".toRegex()
            val prizeRegex = "Prize: X=(\\d+), Y=(\\d+)".toRegex()

            val matchA = buttonRegex.matchEntire(lines[0])!!
            val matchB = buttonRegex.matchEntire(lines[1])!!
            val matchPrize = prizeRegex.matchEntire(lines[2])!!
            Machine(
                matchA.groupValues[1].toLong(), matchA.groupValues[2].toLong(),
                matchB.groupValues[1].toLong(), matchB.groupValues[2].toLong(),
                matchPrize.groupValues[1].toLong(), matchPrize.groupValues[2].toLong(),
            )
        }

private data class Machine(
    val aPlusX: Long, val aPlusY: Long,
    val bPlusX: Long, val bPlusY: Long,
    val prizeX: Long, val prizeY: Long,
) {
    val divideBy = bPlusX * aPlusY - bPlusY * aPlusX
    val buttonA = (prizeY * bPlusX - prizeX * bPlusY) / divideBy
    val buttonB = (prizeX * aPlusY - prizeY * aPlusX) / divideBy
    val cost =
        if (buttonA * aPlusX + buttonB * bPlusX == prizeX && buttonA * aPlusY + buttonB * bPlusY == prizeY)
            buttonA * 3L + buttonB
        else
            0L
}

private const val EXAMPLE: String = """Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279"""

class Day13Test {
    @Test
    fun `part1 example`() = assertThat(::part1 invokedWith EXAMPLE).isEqualTo(480)

    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(13)).isEqualTo(35082)

    @Test
    fun `part2 example`() = assertThat(::part2 invokedWith EXAMPLE).isEqualTo(875318608908)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(13)).isEqualTo(82570698600470)
}
