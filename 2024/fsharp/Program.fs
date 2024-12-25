module AoC.Program

open System
open System.Diagnostics
open AoC
open AoC.Puzzle

type TimedSolution<'TAnswer1, 'TAnswer2> =
    { Day: Day
      Solution1: 'TAnswer1
      Solution2: 'TAnswer2
      Elapsed1: TimeSpan
      Elapsed2: TimeSpan }

let timedSolveWith part1 part2 puzzle =
    let stopwatch = Stopwatch()
    stopwatch.Start()
    let solution1 = part1 puzzle.Input
    let elapsed1 = stopwatch.Elapsed
    stopwatch.Restart()
    let solution2 = part2 puzzle.Input
    let elapsed2 = stopwatch.Elapsed

    { Day = puzzle.Day
      Solution1 = solution1
      Solution2 = solution2
      Elapsed1 = elapsed1
      Elapsed2 = elapsed2 }

let log timedSolve =
    match timedSolve.Day with
    | Day day -> printf $"%3d{day}"

    printf " | "
    printf $"%-40O{timedSolve.Solution1}"
    printf " | "
    printf $"%-14f{timedSolve.Elapsed1.TotalMilliseconds}"
    printf " | "
    printf $"%-40O{timedSolve.Solution2}"
    printf " | "
    printfn $"%-14f{timedSolve.Elapsed2.TotalMilliseconds}"

let logTimedSolve day part1 part2 = Day day |> withInput FromFile |> timedSolveWith part1 part2 |> log

[<EntryPoint>]
let main _ =
    printfn "Day | Solution 1                               | Time (ms)      | Solution 2                               | Time (ms)      "
    printfn "----+------------------------------------------+----------------+------------------------------------------+----------------"

    logTimedSolve 00 Day00.part1 Day00.part2
    logTimedSolve 01 Day01.part1 Day01.part2
    logTimedSolve 02 Day02.part1 Day02.part2
    logTimedSolve 03 Day03.part1 Day03.part2
    logTimedSolve 04 Day04.part1 Day04.part2
    logTimedSolve 05 Day05.part1 Day05.part2
    logTimedSolve 06 Day06.part1 Day06.part2
    logTimedSolve 07 Day07.part1 Day07.part2
    logTimedSolve 08 Day08.part1 Day08.part2
    logTimedSolve 09 Day09.part1 Day09.part2
    logTimedSolve 10 Day10.part1 Day10.part2
    logTimedSolve 11 Day11.part1 Day11.part2
    logTimedSolve 12 Day12.part1 Day12.part2
    logTimedSolve 13 Day13.part1 Day13.part2
    logTimedSolve 14 (Day14.part1 { Width = 101; Height = 103 }) (Day14.part2 { Width = 101; Height = 103 })
    logTimedSolve 15 Day15.part1 Day15.part2
    logTimedSolve 16 Day16.part1 Day16.part2
    logTimedSolve 17 Day17.part1 Day17.part2
    logTimedSolve 19 Day19.part1 Day19.part2
    logTimedSolve 20 (Day20.part1 100) (Day20.part2 100)
    logTimedSolve 21 Day21.part1 Day21.part2
    logTimedSolve 22 Day22.part1 Day22.part2
    logTimedSolve 23 Day23.part1 Day23.part2
    logTimedSolve 24 Day24.part1 Day24.part2
    logTimedSolve 25 Day25.part1 (fun _ -> "Goodbye AoC, until next year!")

    0
