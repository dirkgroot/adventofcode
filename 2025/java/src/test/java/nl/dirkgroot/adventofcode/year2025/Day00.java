package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day00 implements AoCTest {
    long part1(String input) {
        return input.length();
    }

    long part2(String input) {
        return input.length() * 2L;
    }

    static final String EXAMPLE = "Example!";

    @Test
    public void part1Test() {
        assertEquals(8, invokeWith(this::part1, EXAMPLE));
        assertEquals(26, invokeWith(this::part1, input(0)));
    }

    @Test
    public void part2Test() {
        assertEquals(16, invokeWith(this::part2, EXAMPLE));
        assertEquals(52, invokeWith(this::part2, input(0)));
    }
}
