module AoC.Day23

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

let parse (input: string) =
    input.Split('\n')
    |> Seq.map _.Split('-')
    |> Seq.map (fun p -> p[0], p[1])
    |> Seq.toList

let formGroups allConnections initialGroups =
    allConnections
    |> Seq.fold
        (fun (acc: Set<Set<Set<string>>>) connection ->
            let existingGroups =
                acc
                |> Seq.filter (fun group ->
                    if group.Count = 1 then
                        group
                        |> Set.exists (fun groupConnection ->
                            groupConnection |> Set.intersect connection |> Set.isEmpty |> not)
                    else
                        connection
                        |> Set.forall (fun computer ->
                            group
                            |> Set.exists (fun groupConnection -> groupConnection |> Set.contains computer)))

            existingGroups
            |> Seq.fold (fun acc group -> acc |> Set.remove group |> Set.add (group |> Set.add connection)) acc)
        initialGroups

let rec makeGroups (connections: (string * string) list) =
    match connections with
    | []
    | [ _ ] -> []
    | (c1, c2) :: rest ->
        let candidates =
            rest
            |> Seq.filter (fun (rc1, rc2) -> rc1 = c1 || rc2 = c1 || rc1 = c2 || rc2 = c2)

        let p =
            candidates
            |> Seq.collect (fun (conn1c1, conn1c2) ->
                candidates
                |> Seq.filter (fun (conn2c1, conn2c2) -> Set [ conn1c1; conn1c2; conn2c1; conn2c2 ] |> Set.count > 2)
                |> Seq.map (fun (conn2c1, conn2c2) -> Set [ c1; c2; conn1c1; conn1c2; conn2c1; conn2c2 ]))
            |> Seq.filter (fun s -> s.Count = 3)
            |> Seq.distinct
            |> Seq.toList

        p @ makeGroups rest

let part1 (input: string) =
    input
    |> parse
    |> makeGroups
    |> Seq.filter (fun group -> group |> Seq.exists _.StartsWith('t'))
    |> Seq.distinct
    |> Seq.length

let part2 (input: string) =
    let connectedWith =
        input
        |> parse
        |> Seq.fold
            (fun acc (c1, c2) ->
                acc
                |> Map.change c1 (fun old ->
                    match old with
                    | Some c -> Some(Set.add c2 c)
                    | None -> Some(Set.singleton c2))
                |> Map.change c2 (fun old ->
                    match old with
                    | Some c -> Some(Set.add c1 c)
                    | None -> Some(Set.singleton c1)))
            Map.empty

    let cset comp = connectedWith[comp] |> Set.add comp

    let largestGroup =
        connectedWith.Keys
        |> Seq.collect (fun comp ->
            let mySet = cset comp

            connectedWith[comp]
            |> Seq.map (fun cw -> cw, cset cw |> Set.filter (fun ccw -> Set.contains ccw mySet) |> Set.add cw)
            |> Seq.groupBy snd
            |> Seq.map (fun (a, b) -> a, b |> Seq.map fst |> Set |> Set.add comp)
            |> Seq.filter (fun (a, b) -> a = b)
            |> Seq.map fst)
        |> Seq.distinct
        |> Seq.maxBy _.Count

    Seq.sort largestGroup |> String.concat ","

[<Literal>]
let DAY = 23

[<Literal>]
let EXAMPLE =
    "kh-tc
qp-kh
de-cg
ka-co
yn-aq
qp-ub
cg-tb
vc-aq
tb-ka
wh-tc
yn-cg
kh-ub
ta-co
de-co
tc-td
tb-wq
wh-td
ta-ka
td-qp
aq-cg
wq-ub
ub-vc
de-ta
wq-aq
wq-vc
wh-yn
ka-de
kh-ta
co-tc
wh-qp
tb-vc
td-yn"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 7

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 1352

[<Fact>]
let ``part 2 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE)
    |> solvedWith part2
    |> shouldEqual "co,de,ka,ta"

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith part2
    |> shouldEqual "dm,do,fr,gf,gh,gy,iq,jb,kt,on,rg,xf,ze"
