module AoC.Day01

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    input.Split('\n')
    |> Seq.map _.Split("   ")
    |> Seq.map (fun ids -> (int ids[0], int ids[1]))
    |> Seq.fold (fun (left, right) (id1, id2) -> (id1 :: left, id2 :: right)) (List.empty, List.empty)

let part1 (input: string) =
    let left, right = parse input
    let distance (id1, id2) = abs (id1 - id2)

    Seq.zip (Seq.sort left) (Seq.sort right) |> Seq.sumBy distance

let part2 (input: string) =
    let left, right = parse input
    let occurrences = right |> Seq.countBy id |> Map.ofSeq
    let similarityScore id1 = id1 * (occurrences |> Map.tryFind id1 |> Option.defaultValue 0)

    left |> Seq.sumBy similarityScore

[<Literal>]
let DAY = 1

[<Literal>]
let EXAMPLE =
    "3   4
4   3
2   5
1   3
3   9
3   3"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 11

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 2113135

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 31

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 19097157
