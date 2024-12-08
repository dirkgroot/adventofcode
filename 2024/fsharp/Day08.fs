module AoC.Day08

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

type Coord = (struct (int * int))

let parse (input: string) = input.Split('\n') |> Seq.toArray

let antennas (grid: string array) =
    let indices = seq { 0 .. (grid.Length - 1) }

    indices
    |> Seq.collect (fun y -> indices |> Seq.map (fun x -> grid[y][x], Coord(y, x)))
    |> Seq.filter (fun (c, _) -> c <> '.')
    |> Seq.groupBy fst
    |> Seq.map (fun (freq, b) -> freq, (Seq.map snd b |> Seq.toArray))
    |> Map.ofSeq

[<TailCall>]
let rec antinodes acc finder antennas =
    match antennas with
    | n :: rest -> antinodes (rest |> List.fold (fun ans c -> (finder n c) @ ans) acc) finder rest
    | _ -> acc

let countAntinodes finder (grid: string array) =
    let maxXY = grid.Length - 1
    let findAll antennas = antennas |> Array.toList |> antinodes [] finder
    let all = grid |> antennas |> Map.values |> Seq.map findAll |> Seq.collect id
    let distinct = all |> Seq.distinct

    distinct
    |> Seq.filter (fun struct (y, x) -> y >= 0 && y <= maxXY && x >= 0 && x <= maxXY)
    |> Seq.length

let part1 (input: string) =
    let antinodesOfPair struct (y1, x1) struct (y2, x2) =
        let diffX, diffY = x2 - x1, y2 - y1
        [ Coord(y1 - diffY, x1 - diffX); Coord(y2 + diffY, x2 + diffX) ]

    input |> parse |> countAntinodes antinodesOfPair

let part2 (input: string) =
    let grid = input |> parse
    let maxXY = grid.Length - 1

    let antinodesOfPair (a as struct (y1, x1)) (b as struct (y2, x2)) =
        let diffX, diffY = x2 - x1, y2 - y1

        let nextAntinode fn struct (y, x) =
            let yy, xx = fn y diffY, fn x diffX

            if (yy >= 0 && yy <= maxXY && xx >= 0 && xx <= maxXY) then
                Some(Coord(yy, xx), Coord(yy, xx))
            else
                None

        let antinodes1 = Seq.unfold (nextAntinode (-)) (Coord(y1, x1))
        let antinodes2 = Seq.unfold (nextAntinode (+)) (Coord(y1, x1))

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
let ``example antennas`` () = parse EXAMPLE |> antennas |> Map.keys |> Seq.length |> shouldEqual 2

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 14

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 371

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 34

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 1229
