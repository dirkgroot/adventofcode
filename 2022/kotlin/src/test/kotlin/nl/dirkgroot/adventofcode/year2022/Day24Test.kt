package nl.dirkgroot.adventofcode.year2022

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nl.dirkgroot.adventofcode.util.input
import nl.dirkgroot.adventofcode.util.invokedWith

private fun solution1(input: String) = Valley.parse(input).let { valley ->
    generateSequence(valley) { it.next() }
        .takeWhile { it.map[it.destinationY][it.destinationX] != '*' }
        .count()
}

private fun solution2(input: String) = Valley.parse(input).let { valley ->
    val (m1, goal1) = generateSequence(valley) { it.next() }.withIndex()
        .first { (_, v) -> v.map[v.destinationY][v.destinationX] == '*' }

    goal1.reset(goal1.destinationY, goal1.destinationX)
    val (m2, start) = generateSequence(goal1) { it.next() }.withIndex()
        .first { (_, v) -> v.map[Valley.START_Y][Valley.START_X] == '*' }

    start.reset(Valley.START_Y, Valley.START_X)
    val (m3, _) = generateSequence(start) { it.next() }.withIndex()
        .first { (_, v) -> v.map[v.destinationY][v.destinationX] == '*' }

    m1 + m2 + m3
}

private class Valley(val map: Array<Array<Char>>, val blizzards: List<Blizzard>) {
    val height = map.size
    val width = map[0].size

    val destinationY = map.lastIndex
    val destinationX = map.last().lastIndex - 1

    companion object {
        const val START_Y = 0
        const val START_X = 1

        fun parse(input: String): Valley {
            val lines = input.lines()

            val map = lines.mapIndexed { _, line ->
                line.map { c -> if (c == '#' || c == '.') c else '.' }.toTypedArray()
            }.toTypedArray()

            val blizzards = lines.flatMapIndexed { y, line ->
                line.withIndex()
                    .filter { (_, v) -> v != '#' && v != '.' }
                    .map { (x, c) -> Blizzard(y, x, Direction.parse(c)) }
            }
            map[START_Y][START_X] = '*'

            return Valley(map, blizzards)
        }
    }

    fun next(): Valley {
        val nextBlizzards = blizzards.map { blizzard ->
            val (dy, dx) = when (blizzard.dir) {
                Direction.U -> -1 to 0
                Direction.D -> 1 to 0
                Direction.L -> 0 to -1
                Direction.R -> 0 to 1
            }
            val path = generateSequence(blizzard.y + dy to blizzard.x + dx) { (y, x) ->
                (y + dy + height) % height to (x + dx + width) % width
            }
            val (newY, newX) = path.first { (y, x) -> map[y][x] != '#' }
            Blizzard(newY, newX, blizzard.dir)
        }

        val nextMap = Array(map.size) { i -> map[i].copyOf() }
        (1 until nextMap.lastIndex).forEach { y ->
            (1 until nextMap[y].lastIndex).forEach { x ->
                val setStar = sequenceOf(y to x - 1, y to x + 1, y - 1 to x, y + 1 to x)
                    .filter { (y, x) -> y >= 0 && x < width }
                    .any { (y, x) -> map[y][x] == '*' }

                if (setStar) nextMap[y][x] = '*'
            }
        }
        if (map[START_Y + 1][START_X] == '*') nextMap[START_Y][START_X] = '*'
        if (map[destinationY - 1][destinationX] == '*') nextMap[destinationY][destinationX] = '*'
        nextBlizzards.forEach { (y, x, _) -> nextMap[y][x] = '.' }

        return Valley(nextMap, nextBlizzards)
    }

    fun reset(startY: Int, startX: Int) {
        map.indices.forEach { y -> map[y].indices.forEach { x -> if (map[y][x] != '#') map[y][x] = '.' } }
        map[startY][startX] = '*'
    }

    data class Blizzard(val y: Int, val x: Int, val dir: Direction)
    enum class Direction {
        U, D, L, R;

        companion object {
            fun parse(c: Char) = when (c) {
                '^' -> U
                'v' -> D
                '<' -> L
                '>' -> R
                else -> throw IllegalArgumentException()
            }
        }
    }
}

//===============================================================================================\\

private const val YEAR = 2022
private const val DAY = 24

class Day24Test : StringSpec({
    "example part 1" { ::solution1 invokedWith exampleInput shouldBe 18 }
    "part 1 solution" { ::solution1 invokedWith input(YEAR, DAY) shouldBe 242 }
    "example part 2" { ::solution2 invokedWith exampleInput shouldBe 54 }
    "part 2 solution" { ::solution2 invokedWith input(YEAR, DAY) shouldBe 720 }
})

private val exampleInput =
    """
        #.######
        #>>.<^<#
        #.<..<<#
        #>v.><>#
        #<^v^^>#
        ######.#
    """.trimIndent()
