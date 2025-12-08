package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Long.parseLong;
import static java.lang.Math.powExact;
import static java.lang.Math.sqrt;
import static java.util.Comparator.comparingDouble;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day08 implements AoCTest {
    record Box(long x, long y, long z) {
        static Box parse(String s) {
            var parts = s.split(",");
            return new Box(parseLong(parts[0]), parseLong(parts[1]), parseLong(parts[2]));
        }

        double distanceTo(Box other) {
            return sqrt(powExact(this.z - other.z, 2) + powExact(this.y - other.y, 2) + powExact(this.x - other.x, 2));
        }
    }

    record Distance(Box b1, Box b2, double distance) {
    }

    long part1(String input, int connections) {
        var boxes = parseBoxes(input);
        var circuits = initializeCircuits(boxes);
        var distances = findDistances(boxes);

        distances.stream().limit(connections).forEach(d -> {
            var c1 = findCircuitContainingBox(circuits, d.b1);
            var c2 = findCircuitContainingBox(circuits, d.b2);
            if (c1 != c2) mergeCircuits(c1, c2);
        });

        return circuits.stream().sorted((a, b) -> Integer.compare(b.size(), a.size()))
                .limit(3)
                .map(HashSet::size)
                .reduce(1, (acc, s) -> acc * s);
    }

    long part2(String input) {
        var boxes = parseBoxes(input);
        var circuits = initializeCircuits(boxes);
        var distances = findDistances(boxes);

        for (var d : distances) {
            var c1 = findCircuitContainingBox(circuits, d.b1);
            var c2 = findCircuitContainingBox(circuits, d.b2);
            if (c1 != c2) {
                mergeCircuits(c1, c2);
                if (c1.size() == boxes.size()) return d.b1.x * d.b2.x;
            }
        }

        throw new IllegalStateException("No answer!");
    }

    private static List<Box> parseBoxes(String input) {
        return input.lines().map(Box::parse).toList();
    }

    private static List<HashSet<Box>> initializeCircuits(List<Box> boxes) {
        return boxes.stream().map(b -> new HashSet<>(Set.of(b))).toList();
    }

    private static ArrayList<Distance> findDistances(List<Box> boxes) {
        var distances = new ArrayList<Distance>();

        for (var a = 0; a < boxes.size(); a++) {
            for (var b = 0; b < a; b++) {
                Box boxA = boxes.get(a);
                Box boxB = boxes.get(b);
                distances.add(new Distance(boxA, boxB, boxA.distanceTo(boxB)));
            }
        }

        distances.sort(comparingDouble(Distance::distance));

        return distances;
    }

    private static HashSet<Box> findCircuitContainingBox(List<HashSet<Box>> circuits, Box box) {
        return circuits.stream().filter(c -> c.contains(box)).findFirst().orElseThrow();
    }

    private static void mergeCircuits(HashSet<Box> c1, HashSet<Box> c2) {
        c1.addAll(c2);
        c2.clear();
    }

    static final String EXAMPLE = """
            162,817,812
            57,618,57
            906,360,560
            592,479,940
            352,342,300
            466,668,158
            542,29,236
            431,825,988
            739,650,466
            52,470,668
            216,146,977
            819,987,18
            117,168,530
            805,96,715
            346,949,466
            970,615,88
            941,993,340
            862,61,35
            984,92,344
            425,690,689""";

    @Test
    public void part1Test() {
        assertEquals(40, invokeWith((String input) -> part1(input, 10), EXAMPLE));
        assertEquals(330786, invokeWith((String input) -> part1(input, 1000), input(8)));
    }

    @Test
    public void part2Test() {
        assertEquals(25272, invokeWith(this::part2, EXAMPLE));
        assertEquals(3276581616L, invokeWith(this::part2, input(8)));
    }
}
