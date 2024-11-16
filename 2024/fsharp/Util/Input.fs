module AdventOfCode2024.Util.Input

open System.IO

let inputFile day = $"inputs/%02d{day}.txt"

let lines day : seq<string> = inputFile day |> File.ReadLines
