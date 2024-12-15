module AoC.Utils

let lines (grid: 'a array array) =
    seq { 0 .. (grid.Length - 1) }
    |> Seq.map (fun y -> seq { 0 .. (grid[y].Length - 1) } |> Seq.map (fun x -> (y, x)))

let columns (grid: 'a array array) =
    seq { 0 .. (grid[0].Length - 1) }
    |> Seq.map (fun x -> seq { 0 .. (grid.Length - 1) } |> Seq.map (fun y -> (y, x)))
