module AoC.Day10

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    input.Split('\n')
    |> Seq.map (fun line -> line |> Seq.map (fun c -> int c - int '0') |> Seq.toArray)
    |> Seq.toArray

let neighbors y x (grid: int array array) =
    [| -1, 0; 0, 1; 1, 0; 0, -1 |]
    |> Seq.map (fun (dy, dx) -> y + dy, x + dx)
    |> Seq.filter (fun (y, x) -> y >= 0 && y < grid.Length && x >= 0 && x < grid.Length)

let rec reachableTops acc y x (grid: int array array) =
    let height = grid[y][x]

    if height = 9 then
        Set.add (y, x) acc
    else
        neighbors y x grid
        |> Seq.filter (fun (y2, x2) -> grid[y2][x2] - height = 1)
        |> Seq.fold (fun acc (y2, x2) -> acc + (reachableTops acc y2 x2 grid)) acc

let analyse grid analyser =
    grid
    |> Seq.mapi (fun y line ->
        line
        |> Seq.indexed
        |> Seq.filter (fun (_, i) -> i = 0)
        |> Seq.map (fun (x, _) -> analyser y x grid |> Set.count)
        |> Seq.sum)
    |> Seq.sum

let part1 (input: string) = analyse (parse input) (reachableTops Set.empty)

let rec distinctTrails acc trail y x (grid: int array array) =
    let height = grid[y][x]

    if height = 9 then
        Set.add ((y, x) :: trail) acc
    else
        neighbors y x grid
        |> Seq.filter (fun (y2, x2) -> grid[y2][x2] - height = 1)
        |> Seq.fold (fun acc (y2, x2) -> acc + (distinctTrails acc ((y, x) :: trail) y2 x2 grid)) acc

let part2 (input: string) = analyse (parse input) (distinctTrails Set.empty [])

[<Literal>]
let DAY = 10

[<Literal>]
let EXAMPLE =
    "89010123
78121874
87430965
96549874
45678903
32019012
01329801
10456732"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 36

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 557

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 81

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1062
