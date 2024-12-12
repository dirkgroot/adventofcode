module AoC.Day12

open AoC
open AoC.Puzzle
open AoC.Utils
open FsUnitTyped
open Xunit

let rec floodFill y x (toFill: int) (fillWith: int) (grid: int array array) =
    let maxXY = grid.Length - 1

    if grid[y][x] <> toFill || toFill > 1000 then
        grid
    else
        grid[y][x] <- fillWith

        let neighbors =
            [| -1, 0; 0, 1; 1, 0; 0, -1 |]
            |> Seq.map (fun (dy, dx) -> (y + dy, x + dx))
            |> Seq.filter (fun (y, x) -> y >= 0 && y <= maxXY && x >= 0 && x <= maxXY)

        for y, x in neighbors do
            floodFill y x toFill fillWith grid |> ignore

        grid

let parse (input: string) =
    let grid =
        input.Split('\n')
        |> Seq.map (fun l -> l |> Seq.map int |> Seq.toArray)
        |> Seq.toArray

    lines grid
    |> Seq.concat
    |> Seq.mapi (fun i (y, x) -> floodFill y x (grid[y][x]) (i + 1000) grid)
    |> Seq.last

let valueOf y x (grid: int array array) =
    if (y >= 0 && y < grid.Length && x >= 0 && x < grid.Length) then
        grid[y][x]
    else
        -1

let fences y x (grid: int array array) =
    let neighbors y x = [| -1, 0; 0, 1; 1, 0; 0, -1 |] |> Seq.map (fun (dy, dx) -> (y + dy, x + dx))

    neighbors y x
    |> Seq.map (fun (y, x) -> valueOf y x grid)
    |> Seq.filter (fun c -> c <> grid[y][x])
    |> Seq.length

let part1 (input: string) =
    let grid = input |> parse

    lines grid
    |> Seq.concat
    |> Seq.map (fun (y, x) -> grid[y][x], fences y x grid)
    |> Seq.groupBy fst
    |> Seq.map (fun (_, spot) ->
        let area = spot |> Seq.length
        let perimeter = spot |> Seq.map snd |> Seq.sum
        area * perimeter)
    |> Seq.sum

let sides (grid: int array array) =
    let findSides dy dx lineOrColumn =
        lineOrColumn
        |> Seq.map (fun (y, x) -> valueOf y x grid, valueOf (y + dy) (x + dx) grid)
        |> Seq.fold
            (fun acc (crop, adjacent) ->
                match acc with
                | [] when crop <> adjacent -> [ crop ]
                | _ :: _ when crop = adjacent -> -1 :: acc
                | f :: _ when f <> crop && crop <> adjacent -> crop :: acc
                | _ -> acc)
            []

    let sides (gridCoords: (int * int) seq seq) dy dx = gridCoords |> Seq.collect (findSides dy dx)
    let lines = lines grid
    let columns = columns grid

    [| sides lines -1 0; sides lines 1 0; sides columns 0 -1; sides columns 0 1 |]
    |> Seq.concat
    |> Seq.filter (fun i -> i <> -1)
    |> Seq.countBy id
    |> Map.ofSeq

let part2 (input: string) =
    let grid = parse input
    let patchSizes = grid |> Seq.collect id |> Seq.countBy id |> Map.ofSeq
    let sideCounts = sides grid

    sideCounts |> Map.toSeq |> Seq.map (fun (k, v) -> patchSizes[k] * v) |> Seq.sum

[<Literal>]
let DAY = 12

[<Literal>]
let EXAMPLE =
    "RRRRIICCFF
RRRRIICCCF
VVRRRCCFFF
VVRCCCJFFF
VVVVCJJCFE
VVIVCCJJEE
VVIIICJJEE
MIIIIIJJEE
MIIISIJEEE
MMMISSJEEE"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 1930

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 1421958

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 1206

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 885394
