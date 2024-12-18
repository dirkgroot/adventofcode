module AoC.Day17

open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

type ComboOperand =
    | Literal of int64
    | RegisterA
    | RegisterB
    | RegisterC
    | Reserved

type LiteralOperand = int64

type Instruction =
    | Adv of ComboOperand
    | Bxl of LiteralOperand
    | Bst of ComboOperand
    | Jnz of LiteralOperand
    | Bxc of LiteralOperand
    | Out of ComboOperand
    | Bdv of ComboOperand
    | Cdv of ComboOperand

[<Struct>]
type Computer =
    { a: int64
      b: int64
      c: int64
      ip: int
      output: int64 list
      program: int64 list }

let parseCombo i =
    if i >= 0L && i <= 3L then Literal(i)
    elif i = 4L then RegisterA
    elif i = 5L then RegisterB
    elif i = 6L then RegisterC
    else Reserved

let parseInstruction opcode operand =
    match opcode with
    | 0L -> Adv(parseCombo operand)
    | 1L -> Bxl(operand)
    | 2L -> Bst(parseCombo operand)
    | 3L -> Jnz(operand)
    | 4L -> Bxc(operand)
    | 5L -> Out(parseCombo operand)
    | 6L -> Bdv(parseCombo operand)
    | _ -> Cdv(parseCombo operand)

let comboValue computer operand =
    match operand with
    | Literal i -> int64 i
    | RegisterA -> computer.a
    | RegisterB -> computer.b
    | RegisterC -> computer.c
    | Reserved -> -1

let dv (num: int64) (denom: int64) = num / int64 (2.0 ** float denom)

let perform computer instruction =
    match instruction with
    | Adv c ->
        { computer with
            a = dv computer.a (comboValue computer c)
            ip = computer.ip + 2 }
    | Bdv c ->
        { computer with
            b = dv computer.a (comboValue computer c)
            ip = computer.ip + 2 }
    | Cdv c ->
        { computer with
            c = dv computer.a (comboValue computer c)
            ip = computer.ip + 2 }
    | Bxl i ->
        { computer with
            b = computer.b ^^^ i
            ip = computer.ip + 2 }
    | Bst c ->
        { computer with
            b = (comboValue computer c) % 8L
            ip = computer.ip + 2 }
    | Jnz i ->
        if computer.a = 0L then
            { computer with ip = computer.ip + 2 }
        else
            { computer with ip = int i }
    | Bxc _ ->
        { computer with
            b = computer.b ^^^ computer.c
            ip = computer.ip + 2 }
    | Out c ->
        { computer with
            output = computer.output @ [ (comboValue computer c) % 8L ]
            ip = computer.ip + 2 }

let next computer =
    let opcode = computer.program[computer.ip]
    let operand = computer.program[computer.ip + 1]
    perform computer (parseInstruction opcode operand)

let parse (input: string) =
    let parts = input.Split("\n\n")

    let registers =
        parts[0].Split('\n')
        |> Seq.map (fun r -> int64 (r.Substring(12)))
        |> Seq.toArray

    let program = parts[1].Substring(9).Split(',') |> Seq.map int64 |> Seq.toList

    { a = registers[0]
      b = registers[1]
      c = registers[2]
      ip = 0
      output = []
      program = program }

[<TailCall>]
let rec run computer =
    if computer.ip >= computer.program.Length then
        computer
    else
        run (next computer)

let part1 (input: string) =
    let c = input |> parse |> run
    c.output |> Seq.map _.ToString() |> String.concat ","

let rec minA computer candidate inc index =
    let toTry =
        seq { 0L .. 7L }
        |> Seq.map (fun i -> candidate + i * inc)
        |> Seq.filter (fun i -> i > 0L)
        |> Seq.map (fun a -> a, run { computer with a = a })
        |> Seq.filter (fun (_, c) -> c.output[index] = c.program[index])
        |> Seq.map fst

    if Seq.isEmpty toTry then
        -1L
    elif index = 0 then
        Seq.head toTry
    else
        toTry
        |> Seq.map (fun a -> minA computer a (inc >>> 3) (index - 1))
        |> Seq.filter (fun a -> a <> -1L)
        |> Seq.tryHead
        |> Option.defaultValue -1L

let part2 (input: string) =
    let computer = input |> parse
    let inc = 1L <<< (3 * (computer.program.Length - 1))
    minA computer 0L inc (computer.program.Length - 1)

[<Literal>]
let DAY = 17

[<Literal>]
let EXAMPLE1 =
    "Register A: 729
Register B: 0
Register C: 0

Program: 0,1,5,4,3,0"

[<Literal>]
let EXAMPLE2 =
    "Register A: 2024
Register B: 0
Register C: 0

Program: 0,3,5,4,3,0"

[<Fact>]
let ``part 1 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE1)
    |> solvedWith part1
    |> shouldEqual "4,6,3,5,6,3,5,2,1,0"

[<Fact>]
let ``part 1 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith part1
    |> shouldEqual "1,6,7,4,3,0,5,0,6"

[<Fact>]
let ``part 2 - example`` () =
    Day DAY |> withInput (Value EXAMPLE2) |> solvedWith part2 |> shouldEqual 117440L

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY
    |> withInput FromFile
    |> solvedWith part2
    |> shouldEqual 0o6111276474257155L
