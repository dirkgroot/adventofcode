package nl.dirkgroot.adventofcode.year2020

import nl.dirkgroot.adventofcode.util.Input
import nl.dirkgroot.adventofcode.util.Puzzle

class Day08(input: Input) : Puzzle() {
    private val program by lazy { parseProgram(input.lines()) }

    private fun parseProgram(code: List<String>) = code.map {
        it.split(" ").let { (instruction, argument) -> instruction to argument.toInt() }
    }

    override fun part1() = run().second

    override fun part2() = program.asSequence().withIndex()
        .filter { it.value.first != "acc" }
        .map { run(flip = it.index) }
        .first { (isLoop, _) -> !isLoop }
        .let { (_, acc) -> acc }

    private tailrec fun run(flip: Int = -1, ip: Int = 0, acc: Int = 0, visited: Set<Int> = emptySet()): Pair<Boolean, Int> =
        when {
            ip >= program.size -> false to acc
            visited.contains(ip) -> true to acc
            else -> {
                val (instruction, argument) = instruction(ip, ip == flip)
                when (instruction) {
                    "acc" -> run(flip, ip + 1, acc + argument, visited + ip)
                    "jmp" -> run(flip, ip + argument, acc, visited + ip)
                    "nop" -> run(flip, ip + 1, acc, visited + ip)
                    else -> throw IllegalStateException()
                }
            }
        }

    private fun instruction(index: Int, flip: Boolean) = program[index].let { (instruction, argument) ->
        if (flip) (if (instruction == "jmp") "nop" else "jmp") to argument
        else instruction to argument
    }
}