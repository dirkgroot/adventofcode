module AoC.Day13

open System.Text.RegularExpressions
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

type Machine =
    { aPlusX: int64
      aPlusY: int64
      bPlusX: int64
      bPlusY: int64
      prizeX: int64
      prizeY: int64 }

let parse (input: string) : Machine seq =
    input.Split("\n\n")
    |> Seq.map (fun m ->
        let lines = m.Split('\n')
        let buttonRE = Regex(@"Button .: X\+(\d+), Y\+(\d+)")
        let prizeRE = Regex(@"Prize: X=(\d+), Y=(\d+)")
        let matchA = buttonRE.Match(lines[0])
        let matchB = buttonRE.Match(lines[1])
        let matchPrize = prizeRE.Match(lines[2])

        { aPlusX = int64 matchA.Groups[1].Value
          aPlusY = int64 matchA.Groups[2].Value
          bPlusX = int64 matchB.Groups[1].Value
          bPlusY = int64 matchB.Groups[2].Value
          prizeX = int64 matchPrize.Groups[1].Value
          prizeY = int64 matchPrize.Groups[2].Value })

let cost m =
    let divideBy = m.bPlusX * m.aPlusY - m.bPlusY * m.aPlusX
    let buttonA = (m.prizeY * m.bPlusX - m.prizeX * m.bPlusY) / divideBy
    let buttonB = (m.prizeX * m.aPlusY - m.prizeY * m.aPlusX) / divideBy

    if
        buttonA * m.aPlusX + buttonB * m.bPlusX = m.prizeX
        && buttonA * m.aPlusY + buttonB * m.bPlusY = m.prizeY
    then
        buttonA * 3L + buttonB
    else
        0L

let part1 (input: string) = input |> parse |> Seq.map cost |> Seq.sum

let part2 (input: string) =
    input
    |> parse
    |> Seq.map (fun m ->
        { m with
            prizeX = m.prizeX + 10000000000000L
            prizeY = m.prizeY + 10000000000000L})
    |> Seq.map cost
    |> Seq.sum

[<Literal>]
let DAY = 13

[<Literal>]
let EXAMPLE = "Button A: X+94, Y+34
Button B: X+22, Y+67
Prize: X=8400, Y=5400

Button A: X+26, Y+66
Button B: X+67, Y+21
Prize: X=12748, Y=12176

Button A: X+17, Y+86
Button B: X+84, Y+37
Prize: X=7870, Y=6450

Button A: X+69, Y+23
Button B: X+27, Y+71
Prize: X=18641, Y=10279"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 480L

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 35082L

[<Fact>]
let ``part 2 - example`` () =
    Day DAY
    |> withInput (Value EXAMPLE)
    |> solvedWith part2
    |> shouldEqual 875318608908L

[<Fact>]
let ``part 2 - solution`` () =
    Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 82570698600470L
