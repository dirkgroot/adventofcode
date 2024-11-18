module AoC.Day02

open AoC
open AoC.IntCode
open AoC.Puzzle
open FsUnitTyped
open Xunit

let part1 (input: string) =
    parseIntCode input |> arguments 12L 2L |> run |> result

let part2 (input: string) =
    let program = parseIntCode input

    let n, v =
        [ for i in 0L .. 99L do
              for v in 0L .. 99L -> (i, v) ]
        |> List.find (fun (n, v) -> program |> arguments n v |> run |> result = 19690720L)

    100L * n + v

[<Literal>]
let DAY = 2

[<Fact>]
let ``part 1 - example`` () =
    Day DAY
    |> withInput (Value "1,9,10,3,2,3,11,0,99,30,40,50")
    |> solvedWith (fun i -> i |> parseIntCode |> run |> result)
    |> shouldEqual 3500L

[<Fact>]
let ``part 1 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 6730673L

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 3749L
