import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import util.input
import util.invokedWith
import java.util.*

private fun part1(input: String) = parse(input).cheapest().first
private fun part2(input: String) = parse(input).cheapest().second
private fun parse(input: String) = input.lines().map { it.toCharArray() }.toTypedArray()

private fun Array<CharArray>.cheapest(): Pair<Int, Int> {
    val dist = mutableMapOf<Reindeer, Int>()
    val prev = mutableMapOf<Reindeer, MutableList<Reindeer>>()
    val q = PriorityQueue<Reindeer> { o1, o2 -> dist.getValue(o1).compareTo(dist.getValue(o2)) }
    val start = indices.flatMap { y -> this[0].indices.map { x -> y to x } }
        .first { (y, x) -> this[y][x] == 'S' }
        .let { (y, x) -> Reindeer(y, x, 0, 1) }

    q.add(start)
    dist[start] = 0
    prev[start] = mutableListOf()

    while (q.isNotEmpty()) {
        val u = q.poll()

        u.neighbors()
            .filter { (r, _) -> this[r.y][r.x] != '#' }
            .forEach { (v, cost) ->
                val alt = dist.getValue(u) + cost
                val distV = dist.getOrDefault(v, Int.MAX_VALUE)
                if (alt < distV) {
                    dist[v] = alt
                    prev[v] = mutableListOf(u)
                    q.add(v)
                } else if (alt == distV)
                    prev.getValue(v).add(u)
            }
    }

    val ends = dist.entries.filter { (r, _) -> this[r.y][r.x] == 'E' }

    return ends[0].value to shortestPathTiles(ends.map { (k, _) -> k }, prev)
}

private fun shortestPathTiles(ends: List<Reindeer>, prev: Map<Reindeer, MutableList<Reindeer>>): Int {
    val tiles = mutableSetOf<Pair<Int, Int>>()
    tailrec fun count(ends: List<Reindeer>, prev: Map<Reindeer, MutableList<Reindeer>>): Int {
        ends.forEach { tiles.add(it.y to it.x) }
        val prevs = ends.flatMap { prev.getOrDefault(it, emptyList()) }
        if (prevs.isEmpty()) return tiles.size
        return count(prevs, prev)
    }
    return count(ends, prev)
}

private data class Reindeer(val y: Int, val x: Int, val dy: Int = 0, val dx: Int = 1) {
    fun neighbors() = listOf(copy(y = y + dy, x = x + dx) to 1, rotateCW() to 1000, rotateCCW() to 1000)

    private fun rotateCW(): Reindeer =
        when (dy to dx) {
            0 to 1 -> copy(dy = 1, dx = 0)
            1 to 0 -> copy(dy = 0, dx = -1)
            0 to -1 -> copy(dy = -1, dx = 0)
            else -> copy(dy = 0, dx = 1)
        }

    private fun rotateCCW(): Reindeer =
        when (dy to dx) {
            0 to 1 -> copy(dy = -1, dx = 0)
            -1 to 0 -> copy(dy = 0, dx = -1)
            0 to -1 -> copy(dy = 1, dx = 0)
            else -> copy(dy = 0, dx = 1)
        }
}

class Day16Test {
    private val example: String = """
        ###############
        #.......#....E#
        #.#.###.#.###.#
        #.....#.#...#.#
        #.###.#####.#.#
        #.#.#.......#.#
        #.#.#####.###.#
        #...........#.#
        ###.#.#####.#.#
        #...#.....#.#.#
        #.#.#.###.#.#.#
        #.....#...#.#.#
        #.###.#.#.#.#.#
        #S..#.....#...#
        ###############
    """.trimIndent()

    @Test
    fun `part1 example`() = assertThat(::part1 invokedWith example).isEqualTo(7036)

    @Test
    fun `part1 solution`() = assertThat(::part1 invokedWith input(16)).isEqualTo(89460)

    @Test
    fun `part2 example`() = assertThat(::part2 invokedWith example).isEqualTo(45)

    @Test
    fun `part2 solution`() = assertThat(::part2 invokedWith input(16)).isEqualTo(504)
}
