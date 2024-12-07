import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import util.input
import util.invokedWith

typealias Guard = Pair<Pair<Int, Int>, Pair<Int, Int>>

private fun part1(input: String): Int {
    val grid = parse(input)
    val guard = findGuard(grid) to (-1 to 0)
    return path(guard, grid).map { it.first }.distinct().count()
}

private fun part2(input: String): Int {
    val grid = parse(input)
    val guard = findGuard(grid) to (-1 to 0)
    val obstacles = path(guard, grid).map { it.first }.distinct().filter { it != guard.first }

    return obstacles.map { parseWithObstacle(input, it) }
        .filter { hasLoop(guard, it) }
        .count()
}

private fun hasLoop(guard: Guard, grid: Array<String>): Boolean {
    tailrec fun detect(visited: MutableSet<Guard>, guard: Guard): Boolean =
        if (visited.contains(guard)) true
        else when (val s = step(guard, grid)) {
            null -> false
            else -> detect(visited.apply { add(guard) }, s)
        }
    return detect(mutableSetOf(), guard)
}

private fun parseWithObstacle(input: String, obstacle: Pair<Int, Int>): Array<String> {
    val lines = input.lines().toTypedArray()
    val (y, x) = obstacle
    lines[y] = lines[y].replaceRange(x..x, "O")
    return lines
}

private fun findGuard(grid: Array<String>) = positions(grid).first { (y, x) -> grid[y][x] == '^' }

private fun positions(grid: Array<String>) = grid.flatMapIndexed { y, s -> s.indices.map { y to it } }

private fun path(guard: Guard, grid: Array<String>): Sequence<Guard> =
    generateSequence(guard) { prev -> step(prev, grid) }

private fun step(guard: Guard, grid: Array<String>): Guard? {
    val (pos, dir) = guard
    val (y, x) = pos
    val (dy, dx) = dir
    val (y1, x1) = y + dy to x + dx
    val maxXY = grid.lastIndex

    return when {
        y1 < 0 || y1 > maxXY || x1 < 0 || x1 > maxXY -> null
        grid[y1][x1] == '#' || grid[y1][x1] == 'O' -> rotate(guard)
        else -> (y1 to x1) to dir
    }
}

private fun rotate(guard: Guard): Guard =
    when (guard.second) {
        (-1 to 0) -> guard.first to (0 to 1)
        (0 to 1) -> guard.first to (1 to 0)
        (1 to 0) -> guard.first to (0 to -1)
        else -> guard.first to (-1 to 0)
    }

private fun parse(input: String) = input.lines().toTypedArray()

class Day06Test {
    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(6)).isEqualTo(4778)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(6)).isEqualTo(1618)
}
