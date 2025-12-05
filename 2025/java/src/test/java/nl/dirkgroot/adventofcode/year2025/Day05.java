package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day05 implements AoCTest {
    record Range(long first, long last) {
        public boolean isInRange(long l) {
            return l >= first && l <= last;
        }
    }

    long part1(String input) {
        var parts = input.split("\n\n");
        var ranges = parseRanges(parts[0]);
        var ingredients = Arrays.stream(parts[1].split("\n")).map(Long::parseLong);

        return ingredients
                .filter(i -> ranges.stream().anyMatch(r -> r.isInRange(i)))
                .count();
    }

    long part2(String input) {
        var ranges = new ArrayList<>(parseRanges(input.split("\n\n")[0]));

        ranges.sort(Comparator.comparingLong(Range::first));

        var count = 0L;
        var first = 1L;
        var last = 0L;
        for (var r : ranges) {
            if (r.first > last) {
                count += last - first + 1;
                first = r.first;
                last = r.last;
            } else if (r.last > last) {
                last = r.last;
            }
        }
        count += last - first + 1;

        return count;
    }

    private static @NonNull List<Range> parseRanges(String input) {
        return Arrays.stream(input.split("\n"))
                .map(line -> {
                    var fl = line.split("-");
                    return new Range(Long.parseLong(fl[0]), Long.parseLong(fl[1]));
                })
                .toList();
    }

    static final String EXAMPLE = """
            3-5
            10-14
            16-20
            12-18
            
            1
            5
            8
            11
            17
            32""";

    @Test
    public void part1Test() {
        assertEquals(3, invokeWith(this::part1, EXAMPLE));
        assertEquals(601, invokeWith(this::part1, input(5)));
    }

    @Test
    public void part2Test() {
        assertEquals(14, invokeWith(this::part2, EXAMPLE));
        assertEquals(367899984917516L, invokeWith(this::part2, input(5)));
    }
}
