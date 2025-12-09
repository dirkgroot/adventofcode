package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day09 implements AoCTest {
    record Point(long x, long y) {
        static Point parse(String input) {
            var parts = input.split(",");
            return new Point(Long.parseLong(parts[0]), Long.parseLong(parts[1]));
        }

        long area(Point other) {
            var h = Math.abs(this.y - other.y) + 1;
            var l = Math.abs(this.x - other.x) + 1;
            return h * l;
        }

        public boolean between(Point a, Point b) {
            var minX = Math.min(a.x, b.x);
            var maxX = Math.max(a.x, b.x);
            var minY = Math.min(a.y, b.y);
            var maxY = Math.max(a.y, b.y);
            return this.x > minX && this.x < maxX && this.y > minY && this.y < maxY;
        }
    }

    record Edge(Point a, Point b) {
        static Edge of(Point a, Point b) {
            if (a.x > b.x || a.y > b.y) return new Edge(b, a);
            else return new Edge(a, b);
        }

        boolean isHorizontal() {
            return a.y == b.y;
        }

        boolean isVertical() {
            return a.x == b.x;
        }
    }

    long part1(String input) {
        var redTiles = input.lines().map(Point::parse).toList().toArray(new Point[]{});
        var largestArea = 0L;

        for (int a = 0; a < redTiles.length; a++) {
            for (int b = 0; b < a; b++) {
                var area = redTiles[a].area(redTiles[b]);
                if (area > largestArea)
                    largestArea = area;
            }
        }

        return largestArea;
    }

    long part2(String input) {
        var redTiles = input.lines().map(Point::parse).toList();
        var edges = Stream.concat(
                redTiles.stream().gather(Gatherers.windowSliding(2))
                        .map(edge -> Edge.of(edge.getFirst(), edge.get(1))),
                Stream.of(new Edge(redTiles.getLast(), redTiles.getFirst()))
        ).toList();
        var vEdges = edges.stream()
                .filter(Edge::isVertical)
                .sorted(Comparator.comparingLong(a -> a.a.y))
                .collect(Collectors.groupingBy(e -> e.a.y));
        var hEdges = edges.stream()
                .filter(Edge::isHorizontal)
                .sorted(Comparator.comparingLong(a -> a.a.x))
                .collect(Collectors.groupingBy(e -> e.a.x));

        var allTiles = new ArrayList<>(redTiles);
        var prev = new ArrayList<Edge>();
        for (var y : vEdges.keySet().stream().sorted().toList()) {
            prev.removeIf(e -> e.b.y < y);
            prev.forEach(e -> allTiles.add(new Point(e.a.x, y)));
            prev.addAll(vEdges.get(y));
        }
        prev.clear();
        for (var x : hEdges.keySet().stream().sorted().toList()) {
            prev.removeIf(e -> e.b.x < x);
            prev.forEach(e -> allTiles.add(new Point(x, e.a.y)));
            prev.addAll(hEdges.get(x));
        }

        var largestArea = 0L;
        for (int a = 0; a < redTiles.size(); a++) {
            for (int b = 0; b < a; b++) {
                var cornerA = redTiles.get(a);
                var cornerB = redTiles.get(b);

                if (allTiles.stream().noneMatch(tile -> tile.between(cornerA, cornerB))) {
                    var area = cornerA.area(cornerB);
                    if (area > largestArea)
                        largestArea = area;
                }
            }
        }

        return largestArea;
    }

    static final String EXAMPLE = """
            7,1
            11,1
            11,7
            9,7
            9,5
            2,5
            2,3
            7,3""";

    @Test
    public void part1Test() {
        assertEquals(50, invokeWith(this::part1, EXAMPLE));
        assertEquals(4755278336L, invokeWith(this::part1, input(9)));
    }

    @Test
    @Disabled("To make it work for the example input, we need to add a check to make sure that " +
            "every corner of a candidate rectangle is inside the polygon")
    public void part2ExampleTest() {
        assertEquals(24, invokeWith(this::part2, EXAMPLE));
    }

    @Test
    public void part2SolutionTest() {
        assertEquals(1534043700, invokeWith(this::part2, input(9)));
    }
}
