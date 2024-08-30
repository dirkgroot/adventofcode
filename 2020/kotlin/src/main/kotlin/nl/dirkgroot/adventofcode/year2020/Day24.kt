package nl.dirkgroot.adventofcode.year2020

import nl.dirkgroot.adventofcode.util.Input
import nl.dirkgroot.adventofcode.util.Puzzle

class Day24(input: Input) : Puzzle() {
    private val flip by lazy {
        input.lines().map { parseRoute(it) }
    }

    private val directionRegex = "(e|se|sw|w|nw|ne)".toRegex()
    private tailrec fun parseRoute(route: String, directions: List<String> = listOf()): List<String> =
        if (route == "") directions
        else {
            val match = directionRegex.find(route)!!
            parseRoute(route.substring(match.groupValues[1].length), directions + match.groupValues[1])
        }

    override fun part1() = blackTiles().size

    override fun part2(): Int {
        val initialBlackTiles = blackTiles()
        val initialWidth = initialBlackTiles.maxOf { it.first } - initialBlackTiles.minOf { it.first }
        val initialHeight = initialBlackTiles.maxOf { it.second } - initialBlackTiles.minOf { it.second }
        val turns = 100
        val increase = turns * 2 + 4
        val originX = (initialWidth + increase) / 2
        val originY = (initialHeight + increase) / 2
        val blackTiles = initialBlackTiles.map { (x, y) -> originX + x to originY + y }.toMutableSet()
        val grid = Array(initialHeight + increase) { y ->
            BooleanArray(initialWidth + increase) { x -> x to y in blackTiles }
        }

        repeat(turns) {
            val flipToBlack = blackTiles.asSequence()
                .flatMap { (x, y) -> adjacentTiles(x, y) }
                .distinct()
                .filter { (x, y) -> !grid[y][x] && adjacentBlackTiles(grid, x, y) == 2 }
                .toList()
            val flipToWhite = blackTiles.asSequence()
                .filter { (x, y) ->
                    val adjacentBlackTileCount = adjacentBlackTiles(grid, x, y)
                    adjacentBlackTileCount == 0 || adjacentBlackTileCount > 2
                }
                .toList()

            blackTiles.addAll(flipToBlack)
            flipToBlack.forEach { (x, y) -> grid[y][x] = true }
            blackTiles.removeAll(flipToWhite.toSet())
            flipToWhite.forEach { (x, y) -> grid[y][x] = false }
        }

        return grid.sumOf { it.count { isBlack -> isBlack } }
    }

    private fun adjacentBlackTiles(grid: Array<BooleanArray>, x: Int, y: Int) =
        adjacentTiles(x, y).count { (tx, ty) -> grid[ty][tx] }

    private fun adjacentTiles(x: Int, y: Int) =
        listOf(x - 1 to y + 1, x + 1 to y - 1, x - 1 to y, x + 1 to y, x to y + 1, x to y - 1)

    private fun blackTiles() = flip.asSequence()
        .map { getCoordinate(it) }
        .groupBy { it }
        .map { (coord, occurrences) -> coord to occurrences.size }
        .filter { (_, count) -> count % 2 > 0 }
        .map { (coord, _) -> coord }

    private tailrec fun getCoordinate(directions: List<String>, x: Int = 0, y: Int = 0): Pair<Int, Int> =
        if (directions.isEmpty()) x to y
        else when (directions[0]) {
            "e" -> getCoordinate(directions.drop(1), x + 1, y)
            "se" -> getCoordinate(directions.drop(1), x, y + 1)
            "sw" -> getCoordinate(directions.drop(1), x - 1, y + 1)
            "w" -> getCoordinate(directions.drop(1), x - 1, y)
            "nw" -> getCoordinate(directions.drop(1), x, y - 1)
            "ne" -> getCoordinate(directions.drop(1), x + 1, y - 1)
            else -> throw IllegalStateException()
        }
}