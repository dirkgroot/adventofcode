import {readFile} from "node:fs/promises";

export async function input(day: number): Promise<string> {
  return await readFile(`inputs/${day.toString().padStart(2, "0")}.txt`, `utf-8`)
}
