module AoC.Day21

open System
open System.Collections.Generic
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n') |> Seq.map (fun s -> s.ToCharArray() |> Array.toList)

let numericKeypad =
    Map
        [ '7', (0, 0)
          '8', (0, 1)
          '9', (0, 2)
          '4', (1, 0)
          '5', (1, 1)
          '6', (1, 2)
          '1', (2, 0)
          '2', (2, 1)
          '3', (2, 2)
          '0', (3, 1)
          'A', (3, 2) ]

let directionKeypad =
    Map [ '^', (0, 1); 'A', (0, 2); '<', (1, 0); 'v', (1, 1); '>', (1, 2) ]

let numericNeighbors y x dy dx =
    seq {
        if dy > y then
            yield 'v', y + 1, x

        if dy < y then
            yield '^', y - 1, x

        if dx > x then
            yield '>', y, x + 1

        if dx < x then
            yield '<', y, x - 1
    }
    |> Seq.filter (fun (_, y, x) -> (y, x) <> (3, 0))

let numericPaths a b =
    let (y1, x1), (y2, x2) = numericKeypad[a], numericKeypad[b]

    let rec find (acc: char list list) (path: char list) y x : char list list =
        if y = y2 && x = x2 then
            [ path @ [ 'A' ] ]
        else
            numericNeighbors y x y2 x2
            |> Seq.map (fun (c, y, x) -> find acc (path @ [ c ]) y x)
            |> List.concat

    find [] [] y1 x1

let directionNeighbors y x dy dx =
    seq {
        if dy > y then
            yield 'v', y + 1, x

        if dy < y then
            yield '^', y - 1, x

        if dx > x then
            yield '>', y, x + 1

        if dx < x then
            yield '<', y, x - 1
    }
    |> Seq.filter (fun (_, y, x) -> (y, x) <> (0, 0))

let directionPaths a b =
    let (y1, x1), (y2, x2) = directionKeypad[a], directionKeypad[b]

    let rec find (acc: char list list) (path: char list) y x : char list list =
        if y = y2 && x = x2 then
            [ path @ [ 'A' ] ]
        else
            directionNeighbors y x y2 x2
            |> Seq.map (fun (c, y, x) -> find acc (path @ [ c ]) y x)
            |> List.concat

    find [] [] y1 x1

let cache = Dictionary<char * char * int, int64>()

let rec pushDirectionButton prev button robots =
    if cache.ContainsKey((prev, button, robots)) then
        cache[(prev, button, robots)]
    elif robots = 0 then
        1L
    else
        let result =
            directionPaths prev button
            |> Seq.map (fun path ->
                ('A' :: path)
                |> Seq.pairwise
                |> Seq.map (fun (prev, button) -> pushDirectionButton prev button (robots - 1))
                |> Seq.sum)
            |> Seq.min

        cache[(prev, button, robots)] <- result
        result

let pushNumericButton prev button robots =
    numericPaths prev button
    |> Seq.map (fun path ->
        ('A' :: path)
        |> Seq.pairwise
        |> Seq.map (fun (prev, button) -> pushDirectionButton prev button (robots - 1))
        |> Seq.sum)
    |> Seq.min

let rec shortestNumericPath (code: char list) robots =
    ('A' :: code)
    |> Seq.pairwise
    |> Seq.map (fun (prev, button) -> pushNumericButton prev button robots)
    |> Seq.sum

let totalComplexities input robots =
    input
    |> parse
    |> Seq.map (fun code -> code, List.take 3 code |> String.Concat |> int64)
    |> Seq.map (fun (code, n) -> (shortestNumericPath code robots), n)
    |> Seq.map (fun (s, n) -> s * n)
    |> Seq.sum

let part1 (input: string) = totalComplexities input 3
let part2 (input: string) = totalComplexities input 26

[<Literal>]
let DAY = 21

[<Literal>]
let EXAMPLE =
    "029A
980A
179A
456A
379A"

[<Fact>]
let ``numeric paths`` () = numericPaths '7' 'A' |> Seq.length |> shouldEqual 9

[<Fact>]
let ``direction paths`` () = directionPaths 'A' '<' |> Seq.length |> shouldEqual 2

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 126384

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 197560

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith part2
    |> shouldEqual 242337182910752L
