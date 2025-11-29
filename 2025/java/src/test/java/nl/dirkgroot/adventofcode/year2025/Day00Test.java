package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class Day00Test implements AoCTest {
    @Test
    public void part1Solution() {
        assertThat(invokeWith(this::part1, input(0))).isEqualTo(26);
    }

    @Test
    public void part2Solution() {
        assertThat(invokeWith(this::part2, input(0))).isEqualTo(52);
    }

    long part1(String input) {
        return input.length();
    }

    long part2(String input) {
        return input.length() * 2L;
    }
}
