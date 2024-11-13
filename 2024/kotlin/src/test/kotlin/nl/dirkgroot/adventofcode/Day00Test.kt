package nl.dirkgroot.adventofcode

import assertk.assertThat
import assertk.assertions.isEqualTo
import nl.dirkgroot.adventofcode.util.input
import nl.dirkgroot.adventofcode.util.invokedWith
import org.junit.jupiter.api.Test

class Day00Test {
    @Test
    fun `part1 solution`() {
        assertThat(::part1 invokedWith input(0)).isEqualTo(21)
    }

    fun part1(input: String) = input.length
}
