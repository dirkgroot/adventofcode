package nl.dirkgroot.adventofcode.year2022

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nl.dirkgroot.adventofcode.util.input
import nl.dirkgroot.adventofcode.util.invokedWith
import kotlin.math.pow

private fun solution1(input: String) = input.lines().sumOf { it.snafuToLong() }.toSNAFU()

private fun String.snafuToLong() =
    reversed().map {
        when (it) {
            '=' -> -2
            '-' -> -1
            else -> it.digitToInt().toLong()
        }
    }.foldIndexed(0L) { pw, acc, digit ->
        acc + digit * 5.0.pow(pw).toLong()
    }

private fun Long.toSNAFU(): String {
    if (this == 0L) return ""

    val mod = this % 5
    val (carry, n) = if (mod > 2) 1 to mod - 5L else 0 to mod
    val digit = when (n) {
        -2L -> "="
        -1L -> "-"
        else -> n.toString()
    }
    return (this / 5 + carry).toSNAFU() + digit
}

//===============================================================================================\\

private const val YEAR = 2022
private const val DAY = 25

class Day25Test : StringSpec({
    "example part 1" { ::solution1 invokedWith exampleInput shouldBe "2=-1=0" }
    "part 1 solution" { ::solution1 invokedWith input(YEAR, DAY) shouldBe "2--2-0=--0--100-=210" }
})

private val exampleInput =
    """
        1=-0-2
        12111
        2=0=
        21
        2=01
        111
        20012
        112
        1=-1=
        1-12
        12
        1=
        122
    """.trimIndent()
