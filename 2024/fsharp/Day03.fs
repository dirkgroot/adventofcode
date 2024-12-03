module AoC.Day03

open System.Text.RegularExpressions
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let part1 (input: string) =
    Regex.Matches(input, @"mul\((\d+)\,(\d+)\)")
    |> Seq.map (fun m -> int m.Groups[1].Value * int m.Groups[2].Value)
    |> Seq.sum

type Instruction =
    | Do
    | Dont
    | Mul of (int * int)

type State =
    | Doing of int
    | NotDoing of int

let parse input =
    Regex.Matches(input, @"do\(\)|don't\(\)|mul\((\d+),(\d+)\)")
    |> Seq.map (fun m ->
        if m.Value.StartsWith "don't" then Dont
        elif m.Value.StartsWith "do" then Do
        else Mul(int m.Groups[1].Value, int m.Groups[2].Value))

let execute state instruction =
    match state, instruction with
    | Doing result, Dont -> NotDoing result
    | NotDoing result, Do -> Doing result
    | Doing result, Mul(a, b) -> Doing(result + (a * b))
    | _ -> state

let part2 (input: string) =
    match parse input |> Seq.fold execute (Doing 0) with
    | Doing result -> result
    | NotDoing result -> result

[<Literal>]
let DAY = 3

[<Literal>]
let EXAMPLE1 =
    "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))"

[<Literal>]
let EXAMPLE2 =
    "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE1) |> solvedWith part1 |> shouldEqual 161

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 188116424

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE2) |> solvedWith part2 |> shouldEqual 48

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 104245808
