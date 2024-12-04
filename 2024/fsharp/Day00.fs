module AoC.Day00

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let part1 (input: string) = input |> int

let part2 (input: string) = input |> int |> (fun i -> i * 2)

[<Literal>]
let DAY = 0

[<Literal>]
let EXAMPLE = "1"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 1

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 42

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 2

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 84
