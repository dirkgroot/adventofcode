module AoC.Day14

open System.Text.RegularExpressions
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

type Robot = (struct (int * int * int * int))
type Grid = { Width: int; Height: int }

let parse (input: string) =
    let regex = Regex(@"p=(\d+),(\d+) v=(-?\d+),(-?\d+)")

    input.Split('\n')
    |> Seq.map (fun s -> (regex.Match s).Groups |> Seq.map _.Value |> Seq.toArray)
    |> Seq.map (fun m -> Robot(int m[1], int m[2], int m[3], int m[4]))

let move (grid: Grid) (robot: Robot) =
    let struct (x, y, vx, vy) = robot
    let x2 = (x + vx + grid.Width) % grid.Width
    let y2 = (y + vy + grid.Height) % grid.Height
    Robot(x2, y2, vx, vy)

let moveAll (grid: Grid) (robots: Robot seq) = robots |> Seq.map (move grid) |> Seq.toArray

let safetyFactor (grid: Grid) (robots: Robot seq) =
    [| 0, 0, grid.Height / 2, grid.Width / 2
       0, (grid.Width / 2) + 1, grid.Height / 2, grid.Width
       grid.Height / 2 + 1, 0, grid.Height, grid.Width / 2
       grid.Height / 2 + 1, grid.Width / 2 + 1, grid.Height, grid.Width |]
    |> Seq.map (fun (y, x, y2, x2) ->
        robots
        |> Seq.filter (fun struct (rx, ry, _, _) -> rx >= x && rx < x2 && ry >= y && ry < y2)
        |> Seq.length)
    |> Seq.reduce (*)

let part1 (grid: Grid) (input: string) =
    input
    |> parse
    |> Seq.unfold (fun r -> let next = moveAll grid r in Some(next, next))
    |> Seq.take 100
    |> Seq.last
    |> safetyFactor grid

let pictureOf (grid: Grid) (robots: Robot seq) =
    let lines = Array.init grid.Height (fun _ -> Array.create grid.Width '.')
    robots |> Seq.iter (fun struct (x, y, _, _) -> lines[y][x] <- '*')

    lines
    |> Seq.map (fun line -> line |> Seq.map _.ToString())
    |> Seq.map (String.concat "")
    |> String.concat "\n"

let part2 (grid: Grid) (input: string) =
    input
    |> parse
    |> Seq.unfold (fun r -> let next = moveAll grid r in Some(next, next))
    |> Seq.map (pictureOf grid)
    |> Seq.indexed
    |> Seq.find (fun (_, s) -> s.Contains "****************")
    |> (fun (answer, _) -> answer + 1)

[<Literal>]
let DAY = 14

[<Literal>]
let EXAMPLE =
    "p=0,4 v=3,-3
p=6,3 v=-1,-3
p=10,3 v=-1,2
p=2,0 v=2,-1
p=0,0 v=1,3
p=3,0 v=-2,-2
p=7,6 v=-1,-3
p=3,0 v=-1,-2
p=9,3 v=2,3
p=7,3 v=-1,2
p=2,4 v=2,-3
p=9,5 v=-3,-3"

[<Fact>]
let ``part 1 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE)
    |> solvedWith (part1 { Width = 11; Height = 7 })
    |> shouldEqual 12

[<Fact>]
let ``part 1 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith (part1 { Width = 101; Height = 103 })
    |> shouldEqual 231852216

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith (part2 { Width = 101; Height = 103 })
    |> shouldEqual 8159
