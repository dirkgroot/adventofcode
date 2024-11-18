namespace AoC

open System.IO

type Day = Day of int

type Input =
    | FromFile
    | Value of string

type Solver<'TAnswer> = string -> 'TAnswer

type Solution<'TAnswer> = { Day: Day; Value: 'TAnswer }

type Puzzle = { Day: Day; Input: string }

module Puzzle =

    let readFile (day: Day) : string =
        match day with
        | Day day -> File.ReadAllText $"inputs/%02d{day}.txt"

    let withInput (input: Input) (day: Day) : Puzzle =
        { Day = day
          Input =
            match input with
            | FromFile -> readFile day
            | Value s -> s }

    let solvedWith (solver: Solver<'TAnswer>) (puzzle: Puzzle) : 'TAnswer = solver puzzle.Input
