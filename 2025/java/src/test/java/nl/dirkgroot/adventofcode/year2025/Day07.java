package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day07 implements AoCTest {
    record Beam(int x, int y, long timelines) {
    }

    record Result(long splits, long timelines) {
    }

    Result analyzeBeams(String input, int start) {
        var grid = input.lines().map(String::toCharArray).toList().toArray(new char[][]{});
        var splitCount = new AtomicInteger(0);

        var beams = Stream.iterate(
                        List.of(new Beam(start, 0, 1)),
                        bs -> bs.getFirst().y <= grid.length - 1,
                        bs -> bs.stream().flatMap(beam -> {
                                    if (grid[beam.y][beam.x] == '^') {
                                        splitCount.incrementAndGet();
                                        return Stream.of(new Beam(beam.x - 1, beam.y + 1, beam.timelines), new Beam(beam.x + 1, beam.y + 1, beam.timelines));
                                    }
                                    return Stream.of(new Beam(beam.x, beam.y + 1, beam.timelines));
                                })
                                .collect(groupingBy(Beam::x, reducing((a, b1) -> new Beam(a.x, a.y, a.timelines + b1.timelines))))
                                .values().stream().map(Optional::orElseThrow)
                                .toList())
                .reduce((_, second) -> second).orElseThrow();

        return new Result(splitCount.get(), beams.stream().map(b -> b.timelines).mapToLong(l -> l).sum());
    }

    static final String EXAMPLE = """
            .......S.......
            ...............
            .......^.......
            ...............
            ......^.^......
            ...............
            .....^.^.^.....
            ...............
            ....^.^...^....
            ...............
            ...^.^...^.^...
            ...............
            ..^...^.....^..
            ...............
            .^.^.^.^.^...^.
            ...............""";

    @Test
    public void part1Test() {
        assertEquals(21, invokeWith((String input) -> analyzeBeams(input, 7).splits, EXAMPLE));
        assertEquals(1605, invokeWith((String input) -> analyzeBeams(input, 70).splits, input(7)));
    }

    @Test
    public void part2Test() {
        assertEquals(40, invokeWith((String input) -> analyzeBeams(input, 7).timelines, EXAMPLE));
        assertEquals(29893386035180L, invokeWith((String input) -> analyzeBeams(input, 70).timelines, input(7)));
    }
}
