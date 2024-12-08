module AoC.Day08

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) = input.Split('\n') |> Seq.toArray

let antennas (grid: string array) =
    let indices = seq { 0 .. (grid.Length - 1) }

    indices
    |> Seq.collect (fun y -> indices |> Seq.map (fun x -> grid[y][x], (y, x)))
    |> Seq.filter (fun (c, _) -> c <> '.')
    |> Seq.groupBy fst
    |> Seq.map (fun (_, b) -> (Seq.map snd b))

[<TailCall>]
let rec antinodes acc finder antennas =
    match antennas with
    | n :: rest -> antinodes (rest |> List.fold (fun ans c -> (finder n c) @ ans) acc) finder rest
    | _ -> acc

let isInGrid (grid: string array) (y, x) = let maxXY = grid.Length - 1 in y >= 0 && y <= maxXY && x >= 0 && x <= maxXY

let countAntinodes finder grid =
    let findAll antennas = antennas |> Seq.toList |> antinodes [] finder
    let allAntinodes = grid |> antennas |> Seq.map findAll |> Seq.collect id

    allAntinodes |> Seq.filter (isInGrid grid) |> Seq.distinct |> Seq.length

let part1 (input: string) =
    let antinodesOfPair (y1, x1) (y2, x2) =
        let diffX, diffY = x2 - x1, y2 - y1
        [ (y1 - diffY, x1 - diffX); (y2 + diffY, x2 + diffX) ]

    input |> parse |> countAntinodes antinodesOfPair

let part2 (input: string) =
    let grid = input |> parse

    let antinodesOfPair (a as (y1, x1)) (b as (y2, x2)) =
        let diffX, diffY = x2 - x1, y2 - y1

        let nextAntinode fn (y, x) =
            let coord = (fn y diffY, fn x diffX)
            if isInGrid grid coord then Some(coord, coord) else None

        let antinodes1 = Seq.unfold (nextAntinode (-)) (y1, x1)
        let antinodes2 = Seq.unfold (nextAntinode (+)) (y1, x1)

        a :: b :: ([ antinodes1; antinodes2 ] |> Seq.collect id |> Seq.toList)

    grid |> countAntinodes antinodesOfPair

[<Literal>]
let DAY = 8

[<Literal>]
let EXAMPLE =
    "............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............"

[<Fact>]
let ``example antennas`` () = parse EXAMPLE |> antennas |> Seq.length |> shouldEqual 2

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 14

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 371

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 34

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1229
