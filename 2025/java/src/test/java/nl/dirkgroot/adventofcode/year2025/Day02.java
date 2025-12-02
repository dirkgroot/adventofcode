package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.LongStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day02 implements AoCTest {
    long part1(String input) {
        return allNumbers(input)
                .filter(n -> {
                    var str = String.valueOf(n);
                    if (str.length() % 2 != 0) return false;

                    var left = str.substring(0, str.length() / 2);
                    var right = str.substring(str.length() / 2);

                    return left.equals(right);
                })
                .sum();
    }

    long part2(String input) {
        return allNumbers(input)
                .filter(n -> {
                    var str = String.valueOf(n);

                    for (int i = 1; i <= str.length() / 2; i++) {
                        if (str.length() % i != 0) continue;
                        var sub = str.substring(0, i);
                        if (str.equals(sub.repeat(str.length() / i)))
                            return true;
                    }

                    return false;
                })
                .sum();
    }

    private static LongStream allNumbers(String input) {
        return Arrays.stream(input.split(","))
                .map(range -> range.split("-"))
                .map(pair -> {
                    var from = Long.parseLong(pair[0]);
                    var to = Long.parseLong(pair[1]);
                    return LongStream.iterate(from, next -> next <= to, prev -> prev + 1);
                })
                .reduce(LongStream::concat).orElseThrow();
    }

    static final String EXAMPLE = "11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124";

    @Test
    public void part1Test() {
        assertEquals(1227775554L, invokeWith(this::part1, EXAMPLE));
        assertEquals(29940924880L, invokeWith(this::part1, input(2)));
    }

    @Test
    public void part2Test() {
        assertEquals(4174379265L, invokeWith(this::part2, EXAMPLE));
        assertEquals(48631958998L, invokeWith(this::part2, input(2)));
    }
}
