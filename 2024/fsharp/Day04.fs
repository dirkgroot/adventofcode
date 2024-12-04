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
    let width = lines[0].Length
    let maxXY = width - 1
    let diags = lines[0].Length * 2

    seq {
        for diag in 0 .. (diags - 1) do
            let startx = max 0 (diag - width + 1)
            let starty1 = min maxXY diag
            let starty2 = max 0 (maxXY - diag)
            let max = if startx = 0 then starty1 else (width - startx - 1)

            yield seq { for i in 0..max -> lines[starty1 - i][startx + i] }
            yield seq { for i in 0..max -> lines[starty2 + i][startx + i] }
    }

let part1 (input: string) =
    let cols = columns input
    let lines = lines input
    let diagonals = diags input

    let countXMAS chars =
        chars
        |> Seq.windowed 4
        |> Seq.filter (fun s -> s = [| 'X'; 'M'; 'A'; 'S' |])
        |> Seq.length

    let all = Seq.concat [| cols; lines; diagonals |]
    let reverse = all |> Seq.map Seq.rev

    Seq.concat [| all; reverse |] |> Seq.map countXMAS |> Seq.sum

let part2 (input: string) =
    let grid = input.Split('\n') |> Array.map _.ToCharArray()
    let maxXY = grid.Length - 3

    let getX y x =
        [| grid[y][x]
           grid[y][x + 2]
           grid[y + 1][x + 1]
           grid[y + 2][x]
           grid[y + 2][x + 2] |]

    let permutations = set [ "MMASS"; "SSAMM"; "MSAMS"; "SMASM" ]

    seq {
        for y in 0..maxXY do
            for x in 0..maxXY do
                yield getX y x |> String
    }
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
