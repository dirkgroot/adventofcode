module AoC.Day06

open System.Collections.Generic
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n')

let parseWithObstacle (input: string) (y, x) =
    let grid = input.Split('\n')
    grid[y] <- grid[y].Substring(0, x) + "O" + grid[y].Substring(x + 1)
    grid

let positions (grid: string array) =
    seq {
        let dim = { 0 .. (grid.Length - 1) }

        for y in dim do
            for x in dim do
                yield (y, x)
    }

let findGuard (grid: string array) = positions grid |> Seq.find (fun (y, x) -> grid[y][x] = '^')

let rotate ((y, x), (dy, dx)) =
    match (dy, dx) with
    | -1, 0 -> (y, x), (0, 1)
    | 0, 1 -> (y, x), (1, 0)
    | 1, 0 -> (y, x), (0, -1)
    | _ -> (y, x), (-1, 0)

let step ((y, x), (dy, dx)) (grid: string array) =
    let y1, x1 = y + dy, x + dx
    let maxXY = grid.Length - 1

    if (y1 < 0 || y1 > maxXY || x1 < 0 || x1 > maxXY) then
        None
    else if grid[y1][x1] = '#' || grid[y1][x1] = 'O' then
        Some(rotate ((y, x), (dy, dx)))
    else
        Some((y1, x1), (dy, dx))

let path guard grid =
    Some guard
    |> Seq.unfold (fun prev ->
        match prev with
        | Some n -> Some(prev, step n grid)
        | None -> None)
    |> Seq.choose id

[<TailCall>]
let rec detect (visited: HashSet<(int * int) * (int * int)>) (position, direction) grid =
    if visited.Contains((position, direction)) then
        true
    else
        match step (position, direction) grid with
        | None -> false
        | Some(p2, d2) ->
            visited.Add(position, direction) |> ignore
            detect visited (p2, d2) grid

let hasLoop (position, direction) grid = detect (HashSet()) (position, direction) grid

let part1 (input: string) =
    let grid = parse input
    let guard = (findGuard grid), (-1, 0)

    path guard grid |> Seq.map fst |> Seq.distinct |> Seq.length

let part2 (input: string) =
    let grid = parse input
    let guardPosition = findGuard grid
    let guard = guardPosition, (-1, 0)

    let obstacles =
        path guard grid
        |> Seq.map fst
        |> Seq.distinct
        |> Seq.filter ((<>) guardPosition)

    obstacles
    |> Seq.map (parseWithObstacle input)
    |> Seq.filter (hasLoop guard)
    |> Seq.length

[<Literal>]
let DAY = 6

[<Literal>]
let EXAMPLE =
    "....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 41

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 4778

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 6

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1618
