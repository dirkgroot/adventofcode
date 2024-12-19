module AoC.Day19

open System.Collections.Generic
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    let parts = input.Split("\n\n")
    let towels = parts[0].Split(", ")
    let combinations = parts[1].Split('\n')
    towels, combinations

let combinations (combi: string) (towels: string array) =
    let cache = Dictionary<string, int64>()

    let rec possible (combi: string) (towels: string array) =
        if cache.ContainsKey(combi) |> not then
            let b =
                if combi.Length = 0 then
                    1L
                else
                    let candidates = towels |> Seq.filter combi.StartsWith
                    candidates |> Seq.map (fun c -> possible (combi.Substring(c.Length)) towels) |> Seq.sum

            cache[combi] <- b
            b
        else
            cache[combi]

    possible combi towels

let part1 (input: string) =
    let towels, combis = input |> parse
    combis |> Seq.filter (fun c -> combinations c towels > 0) |> Seq.length

let part2 (input: string) =
    let towels, combis = input |> parse
    combis |> Seq.map (fun c -> combinations c towels) |> Seq.sum

[<Literal>]
let DAY = 19

[<Literal>]
let EXAMPLE =
    "r, wr, b, g, bwu, rb, gb, br

brwrr
bggr
gbbr
rrbgbr
ubwu
bwurrg
brgr
bbrgwb"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 6

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 360

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 16

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 577474410989846L
