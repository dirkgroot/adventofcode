import {describe, expect, test} from "vitest";
import {input} from "./input";

const EXAMPLE = `L68\nL30\nR48\nL5\nR60\nL55\nL1\nL99\nR14\nL82`;

const rotate = (input: string) =>
  input.split("\n")
    .map(line => [line[0] === 'R' ? 1 : -1, parseInt(line.substring(1))])
    .reduce(([dial, stopped, passed], [step, dist]) => {
      for (let i = 0; i < dist; i++) {
        dial = (dial + step) % 100;
        if (dial === 0) passed++;
      }
      return [dial, dial === 0 ? stopped + 1 : stopped, passed];
    }, [50, 0, 0])
    .slice(1);

describe('Day 01', () => {
  test('Example', async () => {
    expect(rotate(EXAMPLE)).toStrictEqual([3, 6]);
  });

  test('Real input', async () => {
    expect(rotate(await input(1))).toStrictEqual([1043, 5963]);
  });
});
