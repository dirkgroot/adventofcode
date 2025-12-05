import {describe, expect, test} from "vitest";
import {input} from "./input";

const EXAMPLE = `..@@.@@@@.\n@@@.@.@.@@\n@@@@@.@.@@\n@.@@@@..@.\n@@.@@@@.@@\n.@@@@@@@.@\n.@.@.@.@@@\n@.@@@.@@@@\n.@@@@@@@@.\n@.@.@@@.@.`;

const part1 = (input: string) => {
  const grid = input.split("\n").map(s => s.split(""));
  return accessibleRolls(grid).length;
};

const part2 = (input: string) => {
  const grid = input.split("\n").map(s => s.split(""));
  let count = 0;
  let removed = 0;

  do {
    removed = 0;
    let remove = accessibleRolls(grid)
    remove.forEach(([y, x,]) => grid[y][x] = '.')
    removed = remove.length
    count += removed;
  } while (removed > 0);

  return count;
};

function accessibleRolls(grid: string[][]) {
  return grid.flatMap((_, y) => [...grid[y]].map((c, x) => [y, x, c] as [number, number, string]))
    .filter(([, , c]) => c == "@")
    .filter(([y, x,]) => neighbors(grid, y, x).filter(c => c == "@").length < 4);
}

function neighbors(grid: string[][], y: number, x: number): string[] {
  const result = [];
  for (let dy = -1; dy <= 1; dy++) {
    for (let dx = -1; dx <= 1; dx++) {
      const yy = y + dy;
      const xx = x + dx;
      if (yy >= 0 && yy < grid.length && xx >= 0 && xx < grid[y].length && (dy != 0 || dx != 0))
        result.push(grid[yy][xx]);
    }
  }
  return result;
}

describe('Day 04', () => {
  test('Part 1', async () => {
    expect(part1(EXAMPLE)).toEqual(13);
    expect(part1(await input(4))).toEqual(1445);
  });

  test('Part 2', async () => {
    expect(part2(EXAMPLE)).toEqual(43);
    expect(part2(await input(4))).toEqual(8317);
  });
});
