package nl.dirkgroot.adventofcode.year2022

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nl.dirkgroot.adventofcode.util.input
import nl.dirkgroot.adventofcode.util.invokedWith

private fun solution1(input: String) = parse(input).let { (map, path) ->
    val height = map.size
    val width = map[0].size
    val position = walk(map, path) { pos ->
        val (dr, dc) = pos.facing.toDelta()
        Position((pos.r + dr + height) % height, (pos.c + dc + width) % width, pos.facing)
    }
    position.password
}

private fun solution2(mapping: Map<Position, Position>) = { input: String ->
    parse(input).let { (map, path) ->
        val position = walk(map, path) { pos ->
            val (dr, dc) = pos.facing.toDelta()
            val result = Position(pos.r + dr, pos.c + dc, pos.facing)
            mapping[result] ?: result
        }
        position.password
    }
}

private fun parse(input: String) = input.split("\n\n").let { (map, path) ->
    val mapLines = map.lines()
    val mapWidth = mapLines.maxOf { it.length }

    val parsedMap = mapLines
        .map { line -> CharArray(mapWidth) { x -> line.getOrElse(x) { ' ' } } }
        .toTypedArray<CharArray>()

    val parsedPath = "\\d+|[RL]".toRegex().findAll(path)
        .map { it.value }
        .map { if (it[0].isDigit()) Instruction.Move(it.toInt()) else Instruction.Turn(it) }

    parsedMap to parsedPath
}

private fun walk(map: Array<CharArray>, path: Sequence<Instruction>, next: (Position) -> Position): Position {
    var position = Position(0, map[0].indexOfFirst { it != ' ' }, Facing.R)

    path.forEach { instr ->
        when (instr) {
            is Instruction.Move -> {
                position = generateSequence(position) { pos -> next(pos) }
                    .filter { (r, c, _) -> map[r][c] != ' ' }
                    .take(instr.steps + 1)
                    .takeWhile { (r, c, _) -> map[r][c] != '#' }
                    .last()
            }

            is Instruction.Turn -> {
                position = position.copy(
                    facing = when (instr.direction) {
                        "R" -> position.facing.rotateRight()
                        "L" -> position.facing.rotateLeft()
                        else -> throw IllegalStateException()
                    }
                )
            }
        }
    }
    return position
}

private sealed interface Instruction {
    data class Move(val steps: Int) : Instruction
    data class Turn(val direction: String) : Instruction
}

private enum class Facing(val value: Int) {
    R(0) {
        override fun rotateLeft() = U
        override fun rotateRight() = D
    },
    D(1) {
        override fun rotateLeft() = R
        override fun rotateRight() = L
    },
    L(2) {
        override fun rotateLeft() = D
        override fun rotateRight() = U
    },
    U(3) {
        override fun rotateLeft() = L
        override fun rotateRight() = R
    };

    abstract fun rotateLeft(): Facing
    abstract fun rotateRight(): Facing

    fun toDelta() = when (this) {
        R -> 0 to 1
        D -> 1 to 0
        L -> 0 to -1
        U -> -1 to 0
    }
}

private data class Position(val r: Int, val c: Int, val facing: Facing = Facing.U) {
    operator fun rangeTo(other: Position): Iterable<Position> {
        val rRange = if (other.r < r) r downTo other.r else r..other.r
        val cRange = if (other.c < c) c downTo other.c else c..other.c
        return rRange.flatMap { row -> cRange.map { col -> Position(row, col, facing) } }
    }

    val password get() = (r + 1) * 1000 + (c + 1) * 4 + facing.value
}

//       |00-03|04-07|08-11|12-15|
// ------+-----+-----+-----+-----+
// 00-03 |  -  |  -  |  T  |  -  |
// ------+-----+-----+-----+-----+
// 04-07 |  B  |  L  |  F  |  -  |
// ------+-----+-----+-----+-----+
// 08-11 |  -  |  -  |  D  |  R  |
// ------+-----+-----+-----+-----+
private val exampleMapping =
    generateMapping(-1, -1, 8, 11, Facing.U, 4, 4, 3, 0, Facing.D) +
        generateMapping(0, 3, 7, 7, Facing.L, 4, 4, 4, 7, Facing.D) +
        generateMapping(0, 3, 12, 12, Facing.R, 11, 8, 15, 15, Facing.L) +
        generateMapping(3, 3, 0, 3, Facing.U, 0, 0, 11, 8, Facing.D) +
        generateMapping(3, 3, 4, 7, Facing.U, 0, 3, 8, 8, Facing.R) +
        generateMapping(4, 7, -1, -1, Facing.L, 11, 11, 15, 12, Facing.U) +
        generateMapping(4, 7, 12, 12, Facing.R, 8, 8, 15, 12, Facing.D) +
        generateMapping(7, 7, 12, 15, Facing.U, 7, 4, 11, 11, Facing.L) +
        generateMapping(8, 8, 0, 3, Facing.D, 11, 11, 11, 8, Facing.U) +
        generateMapping(8, 8, 4, 7, Facing.D, 8, 8, 8, 11, Facing.R) +
        generateMapping(8, 11, 7, 7, Facing.L, 7, 7, 7, 4, Facing.U) +
        generateMapping(8, 11, 16, 16, Facing.R, 3, 0, 7, 7, Facing.L) +
        generateMapping(12, 12, 8, 11, Facing.D, 7, 7, 3, 0, Facing.U) +
        generateMapping(12, 12, 12, 15, Facing.D, 7, 4, 11, 11, Facing.L)

//           |000-049|050-099|100-149|
// ----------+-------+--B-L--+--B-D--+
// 000-049   |   - LL|   T   |   R   |DR
// ----------+-------+-------+-------+
// 050-099   |FL - LT|   F   |RD - FR|
// ----------+-------+-------+-------+
// 100-149 TL|   L   |   D   |RR -   |
// ----------+-------+-------+-------+
// 150-199 TU|   B   |DD - BR|   -   |
// ----------+--R-U--+-------+-------+
private val inputMapping =
    /* TT->BL */ generateMapping(-1, -1, 50, 99, Facing.U, 150, 199, 0, 0, Facing.R) +
    /* RT->BD */     generateMapping(-1, -1, 100, 149, Facing.U, 199, 199, 0, 49, Facing.U) +
    /* TL->LL */     generateMapping(0, 49, 49, 49, Facing.L, 149, 100, 0, 0, Facing.R) +
    /* RR->DR */     generateMapping(0, 49, 150, 150, Facing.R, 149, 100, 99, 99, Facing.L) +
    /* RD->FR */     generateMapping(50, 50, 100, 149, Facing.D, 50, 99, 99, 99, Facing.L) +
    /* FL->LT */     generateMapping(50, 99, 49, 49, Facing.L, 100, 100, 0, 49, Facing.D) +
    /* FR->RD */     generateMapping(50, 99, 100, 100, Facing.R, 49, 49, 100, 149, Facing.U) +
    /* LT->FL */     generateMapping(99, 99, 0, 49, Facing.U, 50, 99, 50, 50, Facing.R) +
    /* LL->TL */     generateMapping(100, 149, -1, -1, Facing.L, 49, 0, 50, 50, Facing.R) +
    /* DR->RR */     generateMapping(100, 149, 100, 100, Facing.R, 49, 0, 149, 149, Facing.L) +
    /* DD->BR */     generateMapping(150, 150, 50, 99, Facing.D, 150, 199, 49, 49, Facing.L) +
    /* BL->TU */     generateMapping(150, 199, -1, -1, Facing.L, 0, 0, 50, 99, Facing.D) +
    /* BR->DD */     generateMapping(150, 199, 50, 50, Facing.R, 149, 149, 50, 99, Facing.U) +
    /* BD->RU */     generateMapping(200, 200, 0, 49, Facing.D, 0, 0, 100, 149, Facing.D)

private fun generateMapping(
    fr1: Int, fr2: Int, fc1: Int, fc2: Int, ff: Facing,
    tr1: Int, tr2: Int, tc1: Int, tc2: Int, tf: Facing
) = (Position(fr1, fc1, ff)..Position(fr2, fc2) zip Position(tr1, tc1, tf)..Position(tr2, tc2)).toMap()

//===============================================================================================\\

private const val YEAR = 2022
private const val DAY = 22

class Day22Test : StringSpec({
    "example part 1" { ::solution1 invokedWith exampleInput shouldBe 6032 }
    "part 1 solution" { ::solution1 invokedWith input(YEAR, DAY) shouldBe 88226 }
    "example part 2" { solution2(exampleMapping) invokedWith exampleInput shouldBe 5031 }
    "part 2 solution" { solution2(inputMapping) invokedWith input(YEAR, DAY) shouldBe 57305 }
})

private val exampleInput =
    """
                ...#
                .#..
                #...
                ....
        ...#.......#
        ........#...
        ..#....#....
        ..........#.
                ...#....
                .....#..
                .#......
                ......#.
        
        10R5L5R10L4R5L5
    """.trimIndent()
