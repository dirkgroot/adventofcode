module AoC.Day20

open System
open System.Collections.Generic
open AoC
open AoC.Puzzle
open AoC.Utils
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n') |> Array.map _.ToCharArray()

let distsFromEnd (grid: char array array) =
    let dist = Dictionary<struct (int * int), int>()
    let prev = Dictionary<struct (int * int), struct (int * int)>()
    let q = PriorityQueue<struct (int * int), int>()
    let coords = lines grid |> Seq.concat
    let endY, endX = coords |> Seq.find (fun (y, x) -> grid[y][x] = 'E')
    let maxXY = grid.Length - 1

    dist[struct (endY, endX)] <- 0
    q.Enqueue(struct (endY, endX), 0)

    while q.Count > 0 do
        let struct (uy, ux) as u = q.Dequeue()

        [ struct (uy - 1, ux)
          struct (uy + 1, ux)
          struct (uy, ux - 1)
          struct (uy, ux + 1) ]
        |> Seq.filter (fun (struct (y, x)) -> y >= 0 && y <= maxXY && x >= 0 && x <= maxXY)
        |> Seq.filter (fun (struct (y, x)) -> grid[y][x] <> '#')
        |> Seq.iter (fun v ->
            let alt = dist[u] + 1
            let distV = dist.GetValueOrDefault(v, Int32.MaxValue)

            if alt < distV then
                dist[v] <- alt
                prev[v] <- u
                q.Enqueue(v, distV))

    dist

let viableCheatsFrom y x (dist: Dictionary<struct (int * int), int>) maxLength startDist minSave =
    let cheatStartDist = dist[struct (y, x)]

    seq { (y - maxLength) .. (y + maxLength) }
    |> Seq.collect (fun y -> seq { (x - maxLength) .. (x + maxLength) } |> Seq.map (fun x -> struct (y, x)))
    |> Seq.filter (fun (struct (cy, cx)) -> cy <> y || cx <> x)
    |> Seq.filter dist.ContainsKey
    |> Seq.filter (fun c -> dist[c] < cheatStartDist)
    |> Seq.map (fun (struct (cy, cx) as c) -> (abs (cy - y) + abs (cx - x)), c)
    |> Seq.filter (fun (d, _) -> d <= maxLength)
    |> Seq.map (fun (d, c) -> startDist - cheatStartDist + d + dist[c])
    |> Seq.filter (fun distWithCheat -> distWithCheat <= (startDist - minSave))
    |> Seq.length

let viableCheats (dist: Dictionary<struct (int * int), int>) maxLength startDist minSave =
    dist.Keys
    |> Seq.map (fun (struct (y, x)) -> viableCheatsFrom y x dist maxLength startDist minSave)
    |> Seq.sum

let cheatCount maxLength minSave grid =
    let coords = lines grid |> Seq.concat
    let startY, startX = coords |> Seq.find (fun (y, x) -> grid[y][x] = 'S')
    let dists = distsFromEnd grid
    let startDist = dists[struct (startY, startX)]
    viableCheats dists maxLength startDist minSave

let part1 minSave (input: string) = input |> parse |> cheatCount 2 minSave
let part2 minSave (input: string) = input |> parse |> cheatCount 20 minSave

[<Literal>]
let DAY = 20

[<Literal>]
let EXAMPLE =
    "###############
#...#...#.....#
#.#.#.#.#.###.#
#S#...#.#.#...#
#######.#.#.###
#######.#.#...#
#######.#.###.#
###..E#...#...#
###.#######.###
#...###...#...#
#.#####.#.###.#
#.#...#.#.#...#
#.#.#.#.#.#.###
#...#...#...###
###############"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith (part1 1) |> shouldEqual 44

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith (part1 100) |> shouldEqual 1317

[<Fact>]
let ``part 2 - example`` () =
    Day DAY |> withInput (Value EXAMPLE) |> solvedWith (part2 50) |> shouldEqual 285

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith (part2 100) |> shouldEqual 982474
