namespace AoC

type IntCode =
    { ip: int64; memory: Map<int64, int64> }

module IntCode =
    let parseIntCode (input: string) =
        let memory =
            input.Split ','
            |> Seq.indexed
            |> Seq.map (fun (index, value) -> (int64 index, int64 value))
            |> Map

        { ip = 0L; memory = memory }

    let arguments noun verb intCode =
        { intCode with
            memory = intCode.memory |> Map.add 1L noun |> Map.add 2L verb }

    let result intCode = intCode.memory[0]

    let peek address intCode =
        Map.tryFind address intCode.memory |> Option.defaultValue 0L

    let add intCode =
        let left = peek (intCode.ip + 1L) intCode
        let right = peek (intCode.ip + 2L) intCode
        let destination = peek (intCode.ip + 3L) intCode
        let result = peek left intCode + peek right intCode

        { intCode with
            ip = intCode.ip + 4L
            memory = intCode.memory.Add(destination, result) }

    let multiply intCode =
        let left = peek (intCode.ip + 1L) intCode
        let right = peek (intCode.ip + 2L) intCode
        let destination = peek (intCode.ip + 3L) intCode
        let result = peek left intCode * peek right intCode

        { intCode with
            ip = intCode.ip + 4L
            memory = intCode.memory.Add(destination, result) }

    let rec run intCode =
        let opcode = peek intCode.ip intCode

        match opcode with
        | 1L -> run (add intCode)
        | 2L -> run (multiply intCode)
        | 99L -> intCode
        | _ -> invalidArg "intCode" $"Unknown opcode {opcode}"
