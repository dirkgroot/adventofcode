module AoC.Day07

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    input.Split('\n')
    |> Seq.map (fun line ->
        let parts = line.Split(": ")
        int64 parts[0], (parts[1].Split(' ') |> Seq.map int64 |> Seq.toList))
    |> Seq.toArray

let solution ops equations =
    let rec analyse acc value ns =
        match ns with
        | n :: rest when acc <= value -> ops |> Seq.fold (fun b op -> b || analyse (op acc n) value rest) false
        | _ :: _ -> false
        | [] -> acc = value

    equations
    |> Seq.filter (fun (value, ns) -> analyse (List.head ns) value (List.tail ns))
    |> Seq.map fst
    |> Seq.sum

let concat (i1: int64) (i2: int64) = i1.ToString() + i2.ToString() |> int64

let part1 (input: string) = input |> parse |> solution [| (*); (+) |]
let part2 (input: string) = input |> parse |> solution [| (*); (+); concat |]

[<Literal>]
let DAY = 7

[<Literal>]
let EXAMPLE =
    "190: 10 19
3267: 81 40 27
83: 17 5
156: 15 6
7290: 6 8 6 15
161011: 16 10 13
192: 17 8 14
21037: 9 7 18 13
292: 11 6 16 20"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 3749L

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 4998764814652L

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 11387L

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 37598910447546L
