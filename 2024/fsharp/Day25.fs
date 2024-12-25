module AoC.Day25

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse2 (schematic: string) =
    schematic.Split('\n')
    |> Seq.skip 1
    |> Seq.take 5
    |> Seq.fold
        (fun (struct (a, b, c, d, e)) line ->
            let add = line.ToCharArray() |> Array.map (fun cell -> if cell = '#' then 1 else 0)
            a + add[0], b + add[1], c + add[2], d + add[3], e + add[4])
        struct (0, 0, 0, 0, 0)

let parse (input: string) =
    let schematics = input.Split("\n\n")
    let locks = schematics |> Array.filter (fun s -> s[0] = '#') |> Seq.map parse2
    let keys = schematics |> Array.filter (fun s -> s[0] = '.') |> Seq.map parse2

    locks, keys

let fits (struct (l1, l2, l3, l4, l5)) (struct (k1, k2, k3, k4, k5)) =
    l1 + k1 <= 5 && l2 + k2 <= 5 && l3 + k3 <= 5 && l4 + k4 <= 5 && l5 + k5 <= 5

let part1 (input: string) =
    let locks, keys = parse input
    locks |> Seq.map (fun l -> keys |> Seq.filter (fits l) |> Seq.length) |> Seq.sum

[<Literal>]
let DAY = 25

[<Literal>]
let EXAMPLE =
    "#####
.####
.####
.####
.#.#.
.#...
.....

#####
##.##
.#.##
...##
...#.
...#.
.....

.....
#....
#....
#...#
#.#.#
#.###
#####

.....
.....
#.#..
###..
###.#
###.#
#####

.....
.....
.....
#....
#.#..
#.#.#
#####"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 3

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 3136
