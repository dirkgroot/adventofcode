module AoC.Day24

open System.Text.RegularExpressions
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

type WireState =
    | Activated of bool
    | Unknown

type Wire = string * WireState

type Gate =
    | And of (string * string * string)
    | Or of (string * string * string)
    | Xor of (string * string * string)

let parse (input: string) =
    let regex = Regex(@"(\w+) (AND|OR|XOR) (\w+) -> (\w+)")
    let parts = input.Split("\n\n")

    let wires =
        parts[0].Split('\n')
        |> Seq.map _.Split(": ")
        |> Seq.map (fun input -> input[0], Activated(input[1] = "1"))
        |> Map.ofSeq

    let gates =
        parts[1].Split('\n')
        |> Seq.map (fun line -> (regex.Match line).Groups |> Seq.map _.Value |> Seq.toArray)
        |> Seq.map (fun g ->
            let a1 = if g[1] < g[3] then g[1] else g[3]
            let a2 = if g[1] < g[3] then g[3] else g[1]

            if g[2] = "AND" then And(a1, a2, g[4])
            elif g[2] = "OR" then Or(a1, a2, g[4])
            else Xor(a1, a2, g[4]))
        |> Set

    wires, gates

let toBit wire =
    match wire with
    | Activated b -> if b then 1L else 0L
    | _ -> failwith "Cannot use this wire!"

let wiresToNumber (wires: Map<string, WireState>) =
    let outputWires = wires |> Map.toSeq
    let bits = outputWires |> Seq.sortBy fst |> Seq.map (fun (_, b) -> toBit b)
    bits |> Seq.indexed |> Seq.fold (fun acc (i, bit) -> acc + (bit <<< i)) 0L

let wireState wire =
    match wire with
    | Activated b -> b
    | _ -> failwith "Cannot use this wire!"

let gateResult gate (wires: Map<string, WireState>) =
    match gate with
    | And(a, b, c) -> c, Activated((wireState wires[a]) && (wireState wires[b]))
    | Or(a, b, c) -> c, Activated((wireState wires[a]) || (wireState wires[b]))
    | Xor(a, b, c) -> c, Activated((wireState wires[a]) <> (wireState wires[b]))

let inputWires gate =
    match gate with
    | And(a, b, _)
    | Or(a, b, _)
    | Xor(a, b, _) -> a, b

let run wires gates =
    let wires, _ =
        Seq.unfold
            (fun (wires, gates) ->
                let activatedGates =
                    gates
                    |> Set.filter (fun g ->
                        let a, b = inputWires g
                        Map.containsKey a wires && Map.containsKey b wires)

                let activatedWires =
                    activatedGates
                    |> Seq.map (fun g -> gateResult g wires)
                    |> Seq.fold (fun acc (w, s) -> Map.add w s acc) wires

                let remainingGates = gates - activatedGates

                if Set.isEmpty activatedGates then
                    None
                else
                    Some((activatedWires, remainingGates), (activatedWires, remainingGates)))
            (wires, gates)
        |> Seq.last

    wires |> Map.filter (fun k _ -> k.StartsWith('z')) |> wiresToNumber

let part1 (input: string) = let wires, gates = parse input in run wires gates

let setOutputWire gate o =
    match gate with
    | And(a, b, _) -> And(a, b, o)
    | Or(a, b, _) -> Or(a, b, o)
    | Xor(a, b, _) -> Xor(a, b, o)

let outputWire gate =
    match gate with
    | And(_, _, c)
    | Or(_, _, c)
    | Xor(_, _, c) -> c

let swap (o1: string) (o2: string) (gates: Set<Gate>) =
    let g1 = gates |> Set.toSeq |> Seq.find (fun g -> outputWire g = o1)
    let g2 = gates |> Set.toSeq |> Seq.find (fun g -> outputWire g = o2)
    let newG1 = setOutputWire g1 o2
    let newG2 = setOutputWire g2 o1
    gates |> Set.remove g1 |> Set.remove g2 |> Set.add newG1 |> Set.add newG2

let part2 (input: string) =
    let wires, gates = parse input

    let gates =
        gates
        |> swap "ffj" "z08"
        |> swap "dwp" "kfm"
        |> swap "gjh" "z22"
        |> swap "jdr" "z31"

    let x = wires |> Map.filter (fun id _ -> id.StartsWith('x')) |> wiresToNumber
    let y = wires |> Map.filter (fun id _ -> id.StartsWith('y')) |> wiresToNumber
    let expectedOutcome = x + y
    let actualOutcome = run wires gates
    expectedOutcome - actualOutcome

[<Literal>]
let DAY = 24

[<Literal>]
let EXAMPLE1 =
    "x00: 1
x01: 0
x02: 1
x03: 1
x04: 0
y00: 1
y01: 1
y02: 1
y03: 1
y04: 1

ntg XOR fgs -> mjb
y02 OR x01 -> tnw
kwq OR kpj -> z05
x00 OR x03 -> fst
tgd XOR rvg -> z01
vdt OR tnw -> bfw
bfw AND frj -> z10
ffh OR nrd -> bqk
y00 AND y03 -> djm
y03 OR y00 -> psh
bqk OR frj -> z08
tnw OR fst -> frj
gnj AND tgd -> z11
bfw XOR mjb -> z00
x03 OR x00 -> vdt
gnj AND wpb -> z02
x04 AND y00 -> kjc
djm OR pbm -> qhw
nrd AND vdt -> hwm
kjc AND fst -> rvg
y04 OR y02 -> fgs
y01 AND x02 -> pbm
ntg OR kjc -> kwq
psh XOR fgs -> tgd
qhw XOR tgd -> z09
pbm OR djm -> kpj
x03 XOR y03 -> ffh
x00 XOR y04 -> ntg
bfw OR bqk -> z06
nrd XOR fgs -> wpb
frj XOR qhw -> z04
bqk OR frj -> z07
y03 OR x01 -> nrd
hwm AND bqk -> z03
tgd XOR rvg -> z12
tnw OR pbm -> gnj"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE1) |> solvedWith part1 |> shouldEqual 2024L

[<Fact>]
let ``part 1 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 63168299811048L

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 0
