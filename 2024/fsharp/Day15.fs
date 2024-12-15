module AoC.Day15

open System
open AoC
open AoC.Puzzle
open AoC.Utils
open FsUnitTyped
open Xunit

[<Struct>]
type Puzzle =
    { Grid: char array array
      Robot: int * int
      Moves: string }

let parse (input: string) =
    let parts = input.Split("\n\n")
    let grid = parts[0].Split('\n') |> Array.map _.ToCharArray()

    let robot =
        lines grid |> Seq.collect id |> Seq.find (fun (y, x) -> grid[y][x] = '@')

    { Grid = grid
      Robot = robot
      Moves = parts[1].ReplaceLineEndings("") }

let move y x direction =
    match direction with
    | '<' -> y, x - 1
    | '^' -> y - 1, x
    | '>' -> y, x + 1
    | _ -> y + 1, x

let moveInGrid (grid: char array array) y1 x1 y2 x2 : unit =
    let c = grid[y1][x1]
    grid[y1][x1] <- '.'
    grid[y2][x2] <- c

[<TailCall>]
let rec moveRobotOrBox (grid: char array array) (y: int, x: int) (direction: char) : int * int =
    let y2, x2 = move y x direction

    match grid[y2][x2] with
    | '#' -> y, x
    | 'O' ->
        let boxY, boxX = moveRobotOrBox grid (y2, x2) direction

        if boxY = y2 && boxX = x2 then
            y, x
        else
            moveInGrid grid y x y2 x2
            y2, x2
    | _ ->
        moveInGrid grid y x y2 x2
        y2, x2

let gps (grid: char array array) =
    grid
    |> lines
    |> Seq.collect id
    |> Seq.filter (fun (y, x) -> grid[y][x] = 'O' || grid[y][x] = '[')
    |> Seq.map (fun (y, x) -> y * 100 + x)
    |> Seq.sum

let part1 (input: string) =
    let puzzle = input |> parse
    let _ = puzzle.Moves |> Seq.fold (moveRobotOrBox puzzle.Grid) puzzle.Robot
    gps puzzle.Grid

let scaleUp puzzle =
    let grid =
        lines puzzle.Grid
        |> Seq.map (fun line ->
            line
            |> Seq.collect (fun (y, x) ->
                match puzzle.Grid[y][x] with
                | '#' -> [| '#'; '#' |]
                | 'O' -> [| '['; ']' |]
                | '@' -> [| '@'; '.' |]
                | _ -> [| '.'; '.' |])
            |> Seq.toArray)
        |> Seq.toArray

    { puzzle with
        Grid = grid
        Robot = snd puzzle.Robot, fst puzzle.Robot * 2 }

let adjacentBoxes (grid: char array array) y x direction =
    match direction with
    | '<' -> [| y, x - 2 |]
    | '>' -> [| y, x + 2 |]
    | '^' -> [| y - 1, x - 1; y - 1, x; y - 1, x + 1 |]
    | _ -> [| y + 1, x - 1; y + 1, x; y + 1, x + 1 |]
    |> Seq.filter (fun (y, x) -> grid[y][x] = '[')

let rec canMoveBox (grid: char array array) y x direction =
    let y2, x2 = move y x direction

    let canMoveAdjacentBoxes =
        adjacentBoxes grid y x direction
        |> Seq.fold (fun acc (y, x) -> acc && canMoveBox grid y x direction) true

    if direction = '^' || direction = 'v' then
        match grid[y2][x2], grid[y2][x2 + 1] with
        | '#', _
        | _, '#' -> false
        | _ -> canMoveAdjacentBoxes
    elif direction = '<' && grid[y2][x2] = '#' then
        false
    elif direction = '>' && grid[y2][x2 + 1] = '#' then
        false
    else
        canMoveAdjacentBoxes

let rec moveBox (grid: char array array) y x direction : int * int =
    let y2, x2 = move y x direction

    if canMoveBox grid y x direction then
        let adj = adjacentBoxes grid y x direction
        adj |> Seq.iter (fun (y, x) -> moveBox grid y x direction |> ignore)
        grid[y][x] <- '.'
        grid[y][x + 1] <- '.'
        grid[y2][x2] <- '['
        grid[y2][x2 + 1] <- ']'
        y2, x2
    else
        y, x

let moveRobot2 (grid: char array array) (y: int, x: int) (direction: char) : int * int =
    let y2, x2 = move y x direction

    match grid[y2][x2] with
    | '#' -> y, x
    | '[' ->
        let boxY, boxX = moveBox grid y2 x2 direction

        if boxY = y2 && boxX = x2 then
            y, x
        else
            moveInGrid grid y x y2 x2
            y2, x2
    | ']' ->
        let boxY, boxX = moveBox grid y2 (x2 - 1) direction

        if boxY = y2 && boxX = (x2 - 1) then
            y, x
        else
            moveInGrid grid y x y2 x2
            y2, x2
    | _ ->
        moveInGrid grid y x y2 x2
        y2, x2

let render (grid: char array array) : string =
    grid |> Seq.map (fun line -> String line) |> (fun s -> String.Join("\n", s))

let part2 (input: string) =
    let puzzle = input |> parse |> scaleUp
    let _ = puzzle.Moves |> Seq.fold (moveRobot2 puzzle.Grid) puzzle.Robot
    gps puzzle.Grid

[<Literal>]
let DAY = 15

[<Literal>]
let EXAMPLE =
    "##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 10092

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 1438161

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 9021

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1437981
