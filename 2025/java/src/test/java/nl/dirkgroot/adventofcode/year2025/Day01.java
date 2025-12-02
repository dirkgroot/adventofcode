package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Gatherers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day01 implements AoCTest {
    record State(int dial, int stoppedAtZero, int passedZero) {
    }

    long part1(String input) {
        return doRotations(input).stoppedAtZero;
    }

    long part2(String input) {
        return doRotations(input).passedZero;
    }

    private static State doRotations(String input) {
        return input.lines()
                .gather(Gatherers.fold(() -> new State(50, 0, 0), (state, rotate) -> {
                    var dial = state.dial;
                    var step = (rotate.charAt(0) == 'R') ? 1 : -1;
                    var passedZero = 0;

                    for (var i = 0; i < Integer.parseInt(rotate.substring(1)); i++) {
                        dial = (dial + step) % 100;
                        if (dial == 0) passedZero++;
                    }

                    return new State(dial, state.stoppedAtZero + (dial == 0 ? 1 : 0), state.passedZero + passedZero);
                }))
                .findFirst().orElseThrow();
    }

    static final String EXAMPLE = """
            L68
            L30
            R48
            L5
            R60
            L55
            L1
            L99
            R14
            L82""";

    @Test
    public void part1Test() {
        assertEquals(3, invokeWith(this::part1, EXAMPLE));
        assertEquals(1043, invokeWith(this::part1, input(1)));
    }

    @Test
    public void part2Test() {
        assertEquals(6, invokeWith(this::part2, EXAMPLE));
        assertEquals(5963, invokeWith(this::part2, input(1)));
    }
}
