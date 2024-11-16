module AdventOfCode2024.``Day 00``

open AdventOfCode2024.Util
open FsUnit
open Xunit

[<Fact>]
let ``Part 1`` () =
    let totalLength =
        Input.lines 0 |> Seq.map _.Length |> Seq.reduce (fun acc len -> acc + len)

    totalLength |> should equal 45
