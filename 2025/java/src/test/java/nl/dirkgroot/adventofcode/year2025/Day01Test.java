package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.stream.Gatherers;

import static org.assertj.core.api.Assertions.assertThat;

public class Day01Test implements AoCTest {
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
                .map(line -> line.charAt(0) == 'L'
                        ? -Integer.parseInt(line.substring(1))
                        : Integer.parseInt(line.substring(1)))
                .gather(Gatherers.fold(() -> new State(50, 0, 0), (state, rotate) -> {
                    var dial = state.dial;
                    var step = (rotate > 0) ? 1 : -1;
                    var passedZero = 0;

                    for (var i = 0; i < Math.abs(rotate); i++) {
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
    public void part1Example() {
        assertThat(invokeWith(this::part1, EXAMPLE)).isEqualTo(3);
    }

    @Test
    public void part1Solution() {
        assertThat(invokeWith(this::part1, input(1))).isEqualTo(1043);
    }

    @Test
    public void part2Example() {
        assertThat(invokeWith(this::part2, EXAMPLE)).isEqualTo(6);
    }

    @Test
    public void part2Solution() {
        assertThat(invokeWith(this::part2, input(1))).isEqualTo(5963);
    }
}
