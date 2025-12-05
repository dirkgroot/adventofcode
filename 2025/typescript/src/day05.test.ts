import {describe, expect, test} from "vitest";
import {input} from "./input";

const EXAMPLE = `3-5\n10-14\n16-20\n12-18\n\n1\n5\n8\n11\n17\n32`;

const part1 = (input: string) => {
  const parts = input.split("\n\n");
  const ranges = parseRanges(parts[0]);
  const ingredients = parts[1].split("\n").map(line => parseInt(line));

  return ingredients.filter(i => ranges.find(([first, last]) => i >= first && i <= last) != undefined).length;
};

const part2 = (input: string) => {
  const ranges = parseRanges((input.split("\n\n"))[0]).toSorted(([f1,], [f2,]) => f1 - f2);

  return ranges.reduce(
    ([count, prev_last], [first, last]) => {
      if (first > prev_last) return [count + (last - first + 1), last];
      if (last > prev_last) return [count + (last - prev_last), last];
      return [count, prev_last];
    }, [0, -1]
  )[0];
};

const parseRanges = (input: string): [number, number][] =>
  input.split("\n").map(line => {
    const fl = line.split("-");
    return [parseInt(fl[0]), parseInt(fl[1])]
  });

describe('Day 05', () => {
  test('Part 1', async () => {
    expect(part1(EXAMPLE)).toEqual(3);
    expect(part1(await input(5))).toEqual(601);
  });

  test('Part 2', async () => {
    expect(part2(EXAMPLE)).toEqual(14);
    expect(part2(await input(5))).toEqual(367899984917516);
  });
});
