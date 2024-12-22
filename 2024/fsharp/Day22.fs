module AoC.Day22

open System.Collections.Generic
open AoC
open AoC.Puzzle
open AoC.Utils
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n') |> Array.map int64

let mix a b = a ^^^ b
let prune a = a % 16777216L

let next a =
    let step1 = prune (mix a (a * 64L))
    let step2 = prune (mix step1 (step1 / 32L))
    prune (mix step2 (step2 * 2048L))

let part1 (input: string) =
    input
    |> parse
    |> Seq.map (fun initial ->
        Seq.unfold (fun prev -> Some(next prev, next prev)) initial
        |> Seq.take 2000
        |> Seq.last)
    |> Seq.sum

let optimalSequence (changes: (struct (int64 * int64)) list list) =
    let dictionaries =
        changes
        |> Seq.map (fun c ->
            c
            |> Seq.windowed 4
            |> Seq.fold
                (fun (acc: Dictionary<struct (int64 * int64 * int64 * int64), int64>) sequence ->
                    let key = sequence |> Array.map sndv |> (fun a -> struct (a[0], a[1], a[2], a[3]))
                    let value = sequence |> Seq.last |> fstv

                    if not (acc.ContainsKey(key)) then
                        acc[key] <- value

                    acc)
                (Dictionary<struct (int64 * int64 * int64 * int64), int64>()))
        |> Seq.toList

    let totals = Dictionary<struct (int64 * int64 * int64 * int64), int64>()

    dictionaries
    |> Seq.iter (fun d ->
        d
        |> Seq.iter (fun i ->
            let total = totals.GetValueOrDefault(i.Key) + i.Value
            totals[i.Key] <- total))

    totals.Values |> Seq.max

let part2 (input: string) =
    let changes =
        input
        |> parse
        |> Seq.map (fun initial -> Seq.unfold (fun prev -> Some(prev, next prev)) initial |> Seq.take 2001)
        |> Seq.map (fun secrets -> secrets |> Seq.map (fun secret -> secret % 10L))
        |> Seq.map (fun prices ->
            prices
            |> Seq.pairwise
            |> Seq.map (fun (a, b) -> struct (b, b - a))
            |> Seq.toList)
        |> Seq.toList

    optimalSequence changes

[<Literal>]
let DAY = 22

[<Literal>]
let EXAMPLE1 =
    "1
10
100
2024"

[<Literal>]
let EXAMPLE2 =
    "1
2
3
2024"

[<Fact>]
let ``next secret`` () = next 123 |> next |> shouldEqual 16495136L

[<Fact>]
let ``part 1 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE1)
    |> solvedWith part1
    |> shouldEqual 37327623

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 17262627539L

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE2) |> solvedWith part2 |> shouldEqual 23

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1986
