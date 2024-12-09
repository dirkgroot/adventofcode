module AoC.Day09

open System.Collections.Generic
open AoC
open AoC.Puzzle
open FsUnitTyped
open Xunit

[<Struct>]
type Chunk =
    | Free of freeSize: int * freeID: int
    | File of fileSize: int * fileID: int

let size chunk =
    match chunk with
    | Free(s, _) -> s
    | File(s, _) -> s

let parse (input: string) =
    input
    |> Seq.map (fun c -> int $"{c}")
    |> Seq.mapi (fun i size -> if i % 2 = 0 then File(size, i / 2) else Free(size, 0))
    |> Seq.toArray

let chunkSize chunk =
    match chunk with
    | Free(size, _) -> size
    | File(size, _) -> size

[<TailCall>]
let rec checksum1 (acc: int64) index chunkA indexA chunkB indexB (chunks: Chunk array) =
    match chunks[chunkA], chunks[chunkB] with
    | File(a, _), File _ when chunkA = chunkB && (indexA + indexB) > (a - 1) -> acc
    | File(a, _), _ when indexA = a -> checksum1 acc index (chunkA + 1) 0 chunkB indexB chunks
    | File(_, id), _ -> checksum1 (acc + int64 (index * id)) (index + 1) chunkA (indexA + 1) chunkB indexB chunks
    | _, _ when chunkA >= chunkB -> acc
    | _, Free _ -> checksum1 acc index chunkA indexA (chunkB - 1) 0 chunks
    | Free(a, _), File _ when indexA = a -> checksum1 acc index (chunkA + 1) 0 chunkB indexB chunks
    | Free _, File(b, _) when indexB = b -> checksum1 acc index chunkA indexA (chunkB - 1) 0 chunks
    | Free _, File(_, idB) ->
        checksum1 (acc + int64 (index * idB)) (index + 1) chunkA (indexA + 1) chunkB (indexB + 1) chunks

let part1 (input: string) =
    let chunks = parse input
    let chunkB = chunks.Length - 1
    checksum1 0L 0 0 0 chunkB 0 chunks

let checksum2 index chunkID chunk =
    seq { 0 .. (size chunk - 1) }
    |> Seq.map (fun i -> int64 ((i + index) * chunkID))
    |> Seq.sum

[<TailCall>]
let rec defrag (acc: int64) (visited: HashSet<Chunk>) index (chunksLeft: Chunk list) (chunks: Chunk array) =
    match chunksLeft with
    | c :: rest when visited.Contains c -> defrag acc visited (index + size c) rest chunks
    | File(fileSize, id) as f :: rest ->
        visited.Add f |> ignore
        defrag (acc + (checksum2 index id f)) visited (index + fileSize) rest chunks
    | Free(freeSize, id) as f :: rest ->
        match
            Array.tryFindBack (fun (c: Chunk) -> c.IsFile && size c <= freeSize && not (visited.Contains c)) chunks
        with
        | Some(File(size, srcID) as src) ->
            let checksum = checksum2 index srcID src
            visited.Add src |> ignore
            defrag (acc + checksum) visited (index + size) (Free(freeSize - size, id) :: rest) chunks
        | _ -> defrag acc visited (index + size f) rest chunks
    | _ -> acc

let part2 (input: string) =
    let chunks = parse input
    defrag 0L (HashSet()) 0 (chunks |> Array.toList) chunks

[<Literal>]
let DAY = 9

[<Literal>]
let EXAMPLE = "2333133121414131402"

[<Fact>]
let ``part 1 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part1 |> shouldEqual 1928L

[<Fact>]
let ``part 1 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part1 |> shouldEqual 6330095022244L

[<Fact>]
let ``part 2 - example`` () = Day DAY |> withInput (Value EXAMPLE) |> solvedWith part2 |> shouldEqual 2858L

[<Fact>]
let ``part 2 - solution`` () = Day DAY |> withInput FromFile |> solvedWith part2 |> shouldEqual 6359491814941L
