package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day06 implements AoCTest {
    long part1(String input) {
        var lines = input.lines().toList();
        var numbers = lines.stream().limit(lines.size() - 1)
                .map(line -> Arrays.stream(line.split("\\s+"))
                        .filter(s -> !s.isEmpty())
                        .map(Long::parseLong)
                        .toList()
                ).toList();
        var terms = IntStream.range(0, numbers.getFirst().size()).boxed()
                .map(c -> numbers.stream().map(n -> n.get(c)).toList()).toList();
        var operations = lines.getLast().split("\\s+");

        return solve(terms, operations);
    }

    long part2(String input) {
        var lines = input.lines().toList();
        var numbers = lines.stream().limit(lines.size() - 1).toList();
        var columns = IntStream.range(0, lines.getFirst().length()).boxed()
                .map(c -> numbers.stream().map(n -> n.substring(c, c + 1)).collect(Collectors.joining()))
                .map(String::trim)
                .toList();

        var terms = new ArrayList<List<Long>>();
        var problem = new ArrayList<Long>();
        for (var c : columns) {
            if (c.isEmpty()) {
                terms.add(problem);
                problem = new ArrayList<>();
            } else {
                problem.add(Long.parseLong(c));
            }
        }
        terms.add(problem);

        var operations = lines.getLast().split("\\s+");

        return solve(terms, operations);
    }

    private static long solve(List<List<Long>> terms, String[] operations) {
        var total = 0L;

        for (int i = 0; i < terms.size(); i++) {
            var operation = operations[i];
            var outcome = operation.equals("+") ? 0L : 1L;
            for (var term : terms.get(i)) {
                if (operation.equals("+")) outcome += term;
                else outcome *= term;
            }
            total += outcome;
        }

        return total;
    }

    static final String EXAMPLE = """
            123 328  51 64\s
             45 64  387 23\s
              6 98  215 314
            *   +   *   + \s""";

    @Test
    public void part1Test() {
        assertEquals(4277556, invokeWith(this::part1, EXAMPLE));
        assertEquals(5381996914800L, invokeWith(this::part1, input(6)));
    }

    @Test
    public void part2Test() {
        assertEquals(3263827, invokeWith(this::part2, EXAMPLE));
        assertEquals(9627174150897L, invokeWith(this::part2, input(6)));
    }
}
