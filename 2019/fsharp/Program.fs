module AoC.Program

open System
open System.Diagnostics
open AoC
open AoC.Puzzle

type TimedSolution<'TAnswer> =
    { Day: Day
      Solution1: 'TAnswer
      Solution2: 'TAnswer
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
    printf $"%-30O{timedSolve.Solution1}"
    printf " | "
    printf $"%-14f{timedSolve.Elapsed1.TotalMilliseconds}"
    printf " | "
    printf $"%-30O{timedSolve.Solution2}"
    printf " | "
    printfn $"%-14f{timedSolve.Elapsed2.TotalMilliseconds}"

let logTimedSolve day part1 part2 =
    Day day |> withInput FromFile |> timedSolveWith part1 part2 |> log

[<EntryPoint>]
let main _ =
    printfn "Day | Solution 1                     | Time (ms)      | Solution 2                     | Time (ms)      "
    printfn "----+--------------------------------+----------------+--------------------------------+----------------"

    logTimedSolve 00 Day00.part1 Day00.part2
    logTimedSolve 01 Day01.part1 Day01.part2

    0
