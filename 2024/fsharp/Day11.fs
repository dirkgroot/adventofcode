module AoC.Day11

open System.Collections.Generic
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split(' ') |> Seq.map int64

let digitCount (i: int64) = i.ToString().Length

let cache = Dictionary<struct (int * int64), int64>()

let rec count (step: int) (i: int64) : int64 =
    if cache.ContainsKey(struct (step, i)) then
        cache[step, i]
    else
        let result: int64 =
            if step = 0 then
                1
            else
                let digits: int64 = digitCount i

                if i = 0L then
                    count (step - 1) 1L
                elif digits % 2L = 0L then
                    let d = int64 (10.0 ** double (digits / 2L))
                    (count (step - 1) (i / d)) + (count (step - 1) (i % d))
                else
                    count (step - 1) (i * 2024L)

        cache.Add(struct (step, i), result)
        result

let part1 (input: string) = parse input |> Seq.map (count 25) |> Seq.sum
let part2 (input: string) = parse input |> Seq.map (count 75) |> Seq.sum

[<Literal>]
let DAY = 11

[<Literal>]
let EXAMPLE = "125 17"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 55312

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 231278

[<Fact>]
let ``part 2 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE)
    |> solvedWith part2
    |> shouldEqual 65601038650482L

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith part2
    |> shouldEqual 274229228071551L
