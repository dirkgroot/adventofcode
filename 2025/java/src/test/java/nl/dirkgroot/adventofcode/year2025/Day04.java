package nl.dirkgroot.adventofcode.year2025;

import nl.dirkgroot.adventofcode.util.AoCTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Day04 implements AoCTest {
    long part1(String input) {
        var grid = input.lines().map(String::toCharArray).toArray(char[][]::new);
        var count = 0;

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x] == '.') continue;
                if (neighbors(grid, y, x).stream().filter(c -> c == '@').count() <= 4) count++;
            }
        }

        return count;
    }

    long part2(String input) {
        var grid = input.lines().map(String::toCharArray).toArray(char[][]::new);
        var count = 0;
        var removed = 0;

        do {
            removed = 0;
            for (int y = 0; y < grid.length; y++) {
                for (int x = 0; x < grid[y].length; x++) {
                    if (grid[y][x] == '.') continue;
                    if (neighbors(grid, y, x).stream().filter(c -> c == '@').count() <= 4) {
                        grid[y][x] = '.';
                        removed++;
                        count++;
                    }
                }
            }
        } while (removed > 0);

        return count;
    }

    private ArrayList<Character> neighbors(char[][] grid, int y, int x) {
        var result = new ArrayList<Character>();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int yy = y + dy, xx = x + dx;
                if (xx < 0 || xx >= grid[0].length || yy < 0 || yy >= grid.length) continue;
                result.add(grid[yy][xx]);
            }
        }
        return result;
    }

    static final String EXAMPLE = """
            ..@@.@@@@.
            @@@.@.@.@@
            @@@@@.@.@@
            @.@@@@..@.
            @@.@@@@.@@
            .@@@@@@@.@
            .@.@.@.@@@
            @.@@@.@@@@
            .@@@@@@@@.
            @.@.@@@.@.""";

    @Test
    public void part1Test() {
        assertEquals(13, invokeWith(this::part1, EXAMPLE));
        assertEquals(1445, invokeWith(this::part1, input(4)));
    }

    @Test
    public void part2Test() {
        assertEquals(43, invokeWith(this::part2, EXAMPLE));
        assertEquals(8317, invokeWith(this::part2, input(4)));
    }
}
