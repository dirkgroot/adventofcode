module AoC.Day04

open System
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let columns (input: string) : char seq seq =
    let lines = input.Split('\n')

    Seq.mapi (fun i _ -> i) lines[0]
    |> Seq.map (fun i -> lines |> Seq.map (fun l -> l[i]))

let lines (input: string) : char seq seq = input.Split('\n') |> Seq.map _.ToCharArray()

let diags (input: string) : char seq seq =
    let lines = input.Split('\n')
    let maxXY = lines[0].Length - 1
    let diags = lines[0].Length * 2

    seq {
        for diag in 0 .. (diags - 1) do
            let startx = max 0 (diag - maxXY)
            let starty1 = min maxXY diag
            let starty2 = max 0 (maxXY - diag)
            let max = if startx = 0 then starty1 else (maxXY - startx)

            yield seq { for i in 0..max -> lines[starty1 - i][startx + i] }
            yield seq { for i in 0..max -> lines[starty2 + i][startx + i] }
    }

let part1 (input: string) =
    let countXMAS chars =
        chars
        |> Seq.windowed 4
        |> Seq.filter (fun s -> s = [| 'X'; 'M'; 'A'; 'S' |])
        |> Seq.length

    let forward = Seq.concat [| columns input; lines input; diags input |]
    let reverse = forward |> Seq.map Seq.rev

    Seq.concat [| forward; reverse |] |> Seq.map countXMAS |> Seq.sum

let part2 (input: string) =
    let grid = input.Split('\n') |> Array.map _.ToCharArray()
    let maxXY = grid.Length - 3

    let getX y x =
        [| 0, 0; 0, 2; 1, 1; 2, 0; 2, 2 |]
        |> Seq.map (fun (dy, dx) -> grid[y + dy][x + dx])
        |> String.Concat

    let permutations = set [ "MMASS"; "SSAMM"; "MSAMS"; "SMASM" ]

    seq { for y in 0..maxXY -> { 0..maxXY } |> Seq.map (getX y) }
    |> Seq.concat
    |> Seq.filter (fun x -> Set.contains x permutations)
    |> Seq.length

[<Literal>]
let DAY = 4

[<Literal>]
let EXAMPLE =
    "MMMSXXMASM
MSAMXMSMSA
AMXSXMAAMM
MSAMASMSMX
XMASAMXAMM
XXAMMXXAMA
SMSMSASXSS
SAXAMASAAA
MAMMMXMMMM
MXMXAXMASX"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 18

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 2633

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 9

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1936
