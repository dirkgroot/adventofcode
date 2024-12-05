module AoC.Day05

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    let sections = input.Split("\n\n")

    let rules =
        sections[0].Split('\n')
        |> Seq.map (fun rule -> let is = rule.Split('|') in (int is[0], int is[1]))
        |> Seq.toArray

    let updates =
        sections[1].Split('\n')
        |> Seq.map (fun update -> update.Split(',') |> Seq.map int |> Seq.toList)
        |> Seq.toArray

    rules, updates

let matchesAllRules rules first rest =
    rules
    |> Seq.map (fun (a, b) -> if first <> b then true else List.contains a rest |> not)
    |> Seq.reduce (&&)

[<TailCall>]
let rec isValid rules update =
    match update with
    | fst :: rest -> matchesAllRules rules fst rest && isValid rules rest
    | _ -> true

let middle (list: 'a list) = list[list.Length / 2]

let part1 (input: string) =
    let rules, updates = parse input
    updates |> Seq.filter (isValid rules) |> Seq.map middle |> Seq.sum

let part2 (input: string) =
    let rules, updates = parse input

    let compare a b =
        match (rules |> Seq.tryFind (fun r -> r = (a, b) || r = (b, a))) with
        | Some r -> if r = (a, b) then -1 else 1
        | None -> 0

    updates
    |> Seq.filter (fun update -> isValid rules update |> not)
    |> Seq.map (List.sortWith compare)
    |> Seq.map middle
    |> Seq.sum

[<Literal>]
let DAY = 5

[<Literal>]
let EXAMPLE =
    "47|53
97|13
97|61
97|47
75|29
61|13
75|53
29|13
97|29
53|29
61|53
97|53
61|29
47|13
75|47
97|75
47|61
75|61
47|29
75|13
53|13

75,47,61,53,29
97,61,53,29,13
75,29,13
75,97,47,61,53
61,13,29
97,13,75,29,47"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 143

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 4662

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 123

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 5900
