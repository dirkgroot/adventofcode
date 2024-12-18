import assertk.assertThat
import assertk.assertions.isEqualTo
import util.input
import util.invokedWith
import org.junit.jupiter.api.Test
import java.util.*

private fun part1(maxXY: Int, numberOfBytes: Int) = { input: String ->
    val bytes = parse(input).take(numberOfBytes).toSet()
    shortest(maxXY, maxXY, maxXY, bytes)
}

private fun part2(maxXY: Int) = { input: String ->
    val allBytes = parse(input)
    (1..(allBytes.size))
        .map { allBytes.take(it) }
        .first { bytes -> shortest(maxXY, maxXY, maxXY, bytes.toSet()) == -1 }
        .last()
}

private fun shortest(endY: Int, endX: Int, maxXY: Int, bytes: Set<Pair<Int, Int>>): Int {
    val dist = mutableMapOf<Pair<Int, Int>, Int>()
    val q = PriorityQueue<Pair<Int, Int>> { o1, o2 -> dist.getValue(o1).compareTo(dist.getValue(o2)) }

    q.add(0 to 0)
    dist[0 to 0] = 0

    while (q.isNotEmpty()) {
        val u = q.poll()
        if (endY to endX == u) break

        u.neighbors(maxXY)
            .filter { n -> !bytes.contains(n) }
            .forEach { v ->
                val alt = dist.getValue(u) + 1
                val distV = dist.getOrDefault(v, Int.MAX_VALUE)
                if (alt < distV) {
                    dist[v] = alt
                    q.add(v)
                }
            }
    }

    return dist.getOrDefault(endY to endX, -1)
}

private fun Pair<Int, Int>.neighbors(maxXY: Int): List<Pair<Int, Int>> =
    listOf(first - 1 to second, first to second + 1, first + 1 to second, first to second - 1)
        .filter { (y, x) -> y in 0..maxXY && x in 0..maxXY }

private fun parse(input: String) = input.lines().map { it.split(",").let { (x, y) -> y.toInt() to x.toInt() } }

class Day18Test {
    private val example = """5,4
4,2
4,5
3,0
2,1
6,3
2,4
1,5
0,6
3,3
2,6
5,1
1,2
5,5
2,5
6,5
1,4
0,4
6,4
1,1
6,1
1,0
0,5
1,6
2,0"""

    @Test
    fun `part1 example`() = assertThat(part1(6, 12) invokedWith example).isEqualTo(22)

    @Test
    fun `part1 solution`() = assertThat(part1(70, 1024) invokedWith input(18)).isEqualTo(312)

    @Test
    fun `part2 example`() = assertThat(part2(6) invokedWith example).isEqualTo(1 to 6)

    @Test
    fun `part2 solution`() = assertThat(part2(70) invokedWith input(18)).isEqualTo(26 to 28)
}
