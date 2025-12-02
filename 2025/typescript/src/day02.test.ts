import {describe, expect, test} from "vitest";
import {input} from "./input";

const EXAMPLE = '11-22,95-115,998-1012,1188511880-1188511890,222220-222224,1698522-1698528,446443-446449,38593856-38593862,565653-565659,824824821-824824827,2121212118-2121212124';

const part1 = (input: string): number =>
  allNumbers(input)
    .filter(n => {
      const str = n.toString();
      if (str.length % 2 != 0) return false;

      const left = str.slice(0, str.length / 2);
      const right = str.slice(str.length / 2);

      return left === right;
    })
    .reduce((a, b) => a + b);

const part2 = (input: string): number =>
  allNumbers(input)
    .filter(n => {
      const str = n.toString();

      for (let i = 1; i <= str.length / 2; i++) {
        if (str.length % i != 0) continue;
        const sub = str.slice(0, i);
        if (sub.repeat(str.length / i) === str) return true;
      }

      return false;
    })
    .reduce((a, b) => a + b);

const allNumbers = (input: string): number[] =>
  input.split(',')
    .map(range => range.split('-').map(Number))
    .flatMap(([min, max]) => [...Array(max - min + 1).keys()].map(i => min + i));

describe('Day 02', () => {
  test('Example', async () => {
    expect(part1(EXAMPLE)).toEqual(1227775554);
    expect(part2(EXAMPLE)).toEqual(4174379265);
  });

  test('Real input', async () => {
    expect(part1(await input(2))).toEqual(29940924880);
    expect(part2(await input(2))).toEqual(48631958998);
  });
});
