module AoC.Utils

let lines (grid: 'a array array) =
    let indices = seq { 0 .. (grid.Length - 1) }
    indices |> Seq.map (fun y -> indices |> Seq.map (fun x -> (y, x)))

let columns (grid: 'a array array) =
    let indices = seq { 0 .. (grid.Length - 1) }
    indices |> Seq.map (fun x -> indices |> Seq.map (fun y -> (y, x)))
