module AoC.Day02

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n') |> Seq.map (fun line -> line.Split(' ') |> Seq.map int)

let isSafe (levels: int seq) =
    let inc =
        levels
        |> Seq.windowed 2
        |> Seq.map (fun ll -> ll[1] - ll[0])
        |> Seq.filter (fun diff -> (diff >= 1 && diff <= 3))
        |> Seq.length

    let dec =
        levels
        |> Seq.windowed 2
        |> Seq.map (fun ll -> ll[1] - ll[0])
        |> Seq.filter (fun diff -> (diff >= -3 && diff <= -1))
        |> Seq.length

    inc = (Seq.length levels) - 1 || dec = (Seq.length levels) - 1

let isSafe2 (levels: int seq) =
    let levels = Seq.toArray levels

    if isSafe levels then
        true
    else
        [ 0 .. (Seq.length levels - 1) ]
        |> Seq.map (fun i -> Array.removeAt i levels)
        |> Seq.filter isSafe
        |> Seq.isEmpty
        |> not

let part1 (input: string) = input |> parse |> Seq.filter isSafe |> Seq.length

let part2 (input: string) = input |> parse |> Seq.filter isSafe2 |> Seq.length

[<Literal>]
let DAY = 2

[<Literal>]
let EXAMPLE =
    "7 6 4 2 1
1 2 7 8 9
9 7 6 2 1
1 3 2 4 5
8 6 4 4 1
1 3 6 7 9"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 2

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 282

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 4

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 349
