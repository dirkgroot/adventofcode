package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day03 implements AoCTest {
    long part1(String input) {
        return maxJoltage(input, 2);
    }

    long part2(String input) {
        return maxJoltage(input, 12);
    }

    private long maxJoltage(String input, int batteryCount) {
        return input.lines()
                .map(line -> line.chars().map(c -> Character.digit(c, 10)).toArray())
                .map(batteries -> maxJoltage(batteries, 0, batteryCount))
                .mapToLong(i -> i)
                .sum();
    }

    private long maxJoltage(int[] batteries, int pos, int batteryCount) {
        if (batteryCount == 0) return 0;

        var maxPos = IntStream.range(pos, batteries.length - batteryCount + 1).boxed()
                .max(Comparator.comparingLong(a -> batteries[a])).orElseThrow();

        return batteries[maxPos] * Math.powExact(10L, batteryCount - 1) + maxJoltage(batteries, maxPos + 1, batteryCount - 1);
    }

    static final String EXAMPLE = """
            987654321111111
            811111111111119
            234234234234278
            818181911112111""";

    @Test
    public void part1Test() {
        assertEquals(357, invokeWith(this::part1, EXAMPLE));
        assertEquals(17107, invokeWith(this::part1, input(3)));
    }

    @Test
    public void part2Test() {
        assertEquals(3121910778619L, invokeWith(this::part2, EXAMPLE));
        assertEquals(169349762274117L, invokeWith(this::part2, input(3)));
    }
}
