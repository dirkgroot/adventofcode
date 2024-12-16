module AoC.Day16

open System
open System.Collections.Generic
open AoC
open AoC.Puzzle
open AoC.Utils
open FsUnitTyped
open Xunit

type Reindeer = (struct (int * int * int * int))

let parse (input: string) = input.Split('\n') |> Array.map _.ToCharArray()

let rotateCW ((y, x, dy, dx): Reindeer) =
    match dy, dx with
    | 0, 1 -> Reindeer(y, x, 1, 0)
    | 1, 0 -> Reindeer(y, x, 0, -1)
    | 0, -1 -> Reindeer(y, x, -1, 0)
    | _ -> Reindeer(y, x, 0, 1)

let rotateCCW ((y, x, dy, dx): Reindeer) =
    match dy, dx with
    | 0, 1 -> Reindeer(y, x, -1, 0)
    | 1, 0 -> Reindeer(y, x, 0, 1)
    | 0, -1 -> Reindeer(y, x, 1, 0)
    | _ -> Reindeer(y, x, 0, -1)

let neighbors (y, x, dy, dx as reindeer: Reindeer) =
    seq {
        Reindeer(y + dy, x + dx, dy, dx), 1
        rotateCW reindeer, 1000
        rotateCCW reindeer, 1000
    }

let shortestPathTiles (ends: Reindeer seq) (prev: Dictionary<Reindeer, Reindeer list>) =
    let tiles = HashSet<int * int>()

    let rec count (ends: Reindeer seq) =
        if Seq.isEmpty ends then
            tiles.Count
        else
            let prevs = ends |> Seq.collect (fun r -> prev.GetValueOrDefault(r, []))
            ends |> Seq.iter (fun struct (y, x, _, _) -> tiles.Add((y, x)) |> ignore)
            count prevs

    count ends

let cheapest (grid: char array array) : int * int =
    let dist = Dictionary<Reindeer, int>()
    let prev = Dictionary<Reindeer, Reindeer list>()
    let q = PriorityQueue<Reindeer, int>()

    let start =
        grid
        |> lines
        |> Seq.collect id
        |> Seq.find (fun (y, x) -> grid[y][x] = 'S')
        |> (fun (y, x) -> Reindeer(y, x, 0, 1))

    dist.Add(start, 0)
    prev.Add(start, [])
    q.Enqueue(start, 0)

    while q.Count > 0 do
        let u = q.Dequeue()

        neighbors u
        |> Seq.filter (fun (struct (y, x, _, _), _) -> grid[y][x] <> '#')
        |> Seq.iter (fun (v, cost) ->
            let alt = dist[u] + cost
            let distV = dist.GetValueOrDefault(v, Int32.MaxValue)

            if alt < distV then
                dist[v] <- alt
                prev[v] <- [ u ]
                q.Enqueue(v, alt)
            elif alt = distV then
                prev[v] <- u :: prev[v])

    let ends = dist.Keys |> Seq.filter (fun struct (y, x, _, _) -> grid[y][x] = 'E')

    dist[Seq.head ends], shortestPathTiles ends prev

let part1 (input: string) = input |> parse |> cheapest |> fst
let part2 (input: string) = input |> parse |> cheapest |> snd

[<Literal>]
let DAY = 16

[<Literal>]
let EXAMPLE =
    "###############
#.......#....E#
#.#.###.#.###.#
#.....#.#...#.#
#.###.#####.#.#
#.#.#.......#.#
#.#.#####.###.#
#...........#.#
###.#.#####.#.#
#...#.....#.#.#
#.#.#.###.#.#.#
#.....#...#.#.#
#.###.#.#.#.#.#
#S..#.....#...#
###############"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 7036

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 89460

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 45

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 504
