module AoC.Day01

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let fuelRequired mass = max 0 (mass / 3 - 2)

let rec totalFuelRequired mass =
    let fuel = fuelRequired mass
    if fuel > 0 then fuel + (totalFuelRequired fuel) else fuel

let mapLines fn (text: string) = text.Split('\n') |> Seq.map fn

let part1 (input: string) =
    input |> mapLines int |> Seq.map fuelRequired |> Seq.sum

let part2 (input: string) =
    input |> mapLines int |> Seq.map totalFuelRequired |> Seq.sum

[<Literal>]
let DAY = 1

[<Fact>]
let ``part 1 - example`` () =
    Day DAY |> withInput (Value "100756") |> solvedWith part1 |> shouldEqual 33583

[<Fact>]
let ``part 1 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 3406527

[<Fact>]
let ``part 2 - example`` () =
    Day DAY |> withInput (Value "100756") |> solvedWith part2 |> shouldEqual 50346

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 5106932
