package nl.dirkgroot.adventofcode.year2023

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import nl.dirkgroot.adventofcode.util.input
import nl.dirkgroot.adventofcode.util.invokedWith
import java.util.*

private fun solution1(input: String) = solution(parse(input, false))
private fun solution2(input: String) = solution(parse(input, true))

private fun solution(map: Array<CharArray>): Int {
    val edges = findEdges(map, Vertex(0, 1), Vertex(0, 1))

    return longestPath(edges, Vertex(0, 1), Vertex(map.lastIndex, map[0].lastIndex - 1))
}

private fun findEdges(
    map: Array<CharArray>,
    prevVertex: Vertex,
    startFrom: Vertex,
    edges: MutableSet<Edge> = mutableSetOf(),
    vertices: MutableSet<Vertex> = mutableSetOf(),
): MutableSet<Edge> {
    var prev = prevVertex
    var cur = startFrom
    var n: List<Vertex>
    var cost = if (prevVertex == startFrom) 0 else 1
    var hasSlope = false

    while (true) {
        hasSlope = hasSlope || (map[cur.y][cur.x] in setOf('<', '>', '^', 'v'))
        n = neighbors(map, cur).filterNot { it == prev }
        if (n.isEmpty()) {
            edges.add(Edge(prevVertex, cur, cost))
            if (!hasSlope)
                edges.add(Edge(cur, prevVertex, cost))
            vertices.add(cur)
            return edges
        }
        if (n.size > 1) break
        cost++
        prev = cur
        cur = n[0]
    }

    edges.add(Edge(prevVertex, cur, cost))
    if (!hasSlope)
        edges.add(Edge(cur, prevVertex, cost))
    if (vertices.contains(cur)) return edges
    vertices.add(cur)
    n.forEach { findEdges(map, cur, it, edges, vertices) }

    return edges
}

private fun parse(input: String, ignoreSlopes: Boolean) =
    input.lines().map {
        if (ignoreSlopes)
            it.map { c -> if (c == '.' || c == '#') c else '.' }.toCharArray()
        else
            it.toCharArray()
    }.toTypedArray()

private fun longestPath(edges: MutableSet<Edge>, from: Vertex, to: Vertex): Int {
    data class Candidate(val vertex: Vertex, val cost: Int, val prev: Candidate? = null) {
        fun vertices() = generateSequence(this) { it.prev }
            .map { it.vertex }
            .toSet()
    }

    var optimum = shortestPath(edges, from, to, emptySet())
    var bound = optimum
    val queue = PriorityQueue<Candidate> { a, b -> b.cost.compareTo(a.cost) }
    queue.add(Candidate(from, 0))

    while (queue.isNotEmpty()) {
        val candidate = queue.poll()
        if (candidate.vertex == to && candidate.cost > bound) {
            optimum = candidate.cost
            bound = candidate.cost
        } else {
            val vertices = candidate.vertices()
            edges.filter { it.v1 == candidate.vertex && !vertices.contains(it.v2) }
                .forEach { edge ->
                    val totalEdgeCost = candidate.cost + edge.cost
                    val upperBound = totalEdgeCost + edges
                        .filterNot { vertices.contains(it.v1) || vertices.contains(it.v2) }
                        .sumOf { it.cost }
                    if (upperBound >= bound) {
                        queue.add(Candidate(edge.v2, totalEdgeCost, candidate))
                    }
                }
        }
    }

    return optimum
}

private fun neighbors(map: Array<CharArray>, vertex: Vertex): List<Vertex> {
    val result = mutableListOf<Vertex>()
    if (vertex.y > 0 && (map[vertex.y - 1][vertex.x] == '.' || map[vertex.y - 1][vertex.x] == '^')) {
        result.add(Vertex(vertex.y - 1, vertex.x))
    }
    if (vertex.x < map[0].lastIndex && (map[vertex.y][vertex.x + 1] == '.' || map[vertex.y][vertex.x + 1] == '>')) {
        result.add(Vertex(vertex.y, vertex.x + 1))
    }
    if (vertex.y < map.lastIndex && (map[vertex.y + 1][vertex.x] == '.' || map[vertex.y + 1][vertex.x] == 'v')) {
        result.add(Vertex(vertex.y + 1, vertex.x))
    }
    if (vertex.x > 0 && (map[vertex.y][vertex.x - 1] == '.' || map[vertex.y][vertex.x - 1] == '<')) {
        result.add(Vertex(vertex.y, vertex.x - 1))
    }
    return result
}

private fun shortestPath(edges: Collection<Edge>, from: Vertex, to: Vertex, blocked: Set<Vertex>): Int {
    data class State(val vertex: Vertex, val cost: Int)

    val dist = mutableMapOf(from to 0)
    val queue = PriorityQueue<State> { a, b -> b.cost.compareTo(a.cost) }

    queue.add(State(from, 0))

    while (queue.isNotEmpty()) {
        val u = queue.poll()

        edges.asSequence().filter { !blocked.contains(it.v2) && it.v1 == u.vertex }
            .map { it.v2 to it.cost }
            .forEach { (v, cost) ->
                val alt = dist.getValue(u.vertex) + cost
                if (alt < dist.getOrDefault(v, Int.MAX_VALUE)) {
                    dist[v] = alt
                    queue.add(State(v, alt))
                }
            }
    }

    return dist[to] ?: -1
}

private data class Edge(val v1: Vertex, val v2: Vertex, val cost: Int, var block: Boolean = false)

private data class Vertex(val y: Int, val x: Int)

//===============================================================================================\\

private const val YEAR = 2023
private const val DAY = 23

class Day23Test : StringSpec({
    "example part 1" { ::solution1 invokedWith exampleInput shouldBe 94 }
    "part 1 solution" { ::solution1 invokedWith input(YEAR, DAY) shouldBe 2246 }
    "example part 2" { ::solution2 invokedWith exampleInput shouldBe 154 }
    "part 2 solution" { ::solution2 invokedWith input(YEAR, DAY) shouldBe 6622 }
})

private val exampleInput =
    """
        #.#####################
        #.......#########...###
        #######.#########.#.###
        ###.....#.>.>.###.#.###
        ###v#####.#v#.###.#.###
        ###.>...#.#.#.....#...#
        ###v###.#.#.#########.#
        ###...#.#.#.......#...#
        #####.#.#.#######.#.###
        #.....#.#.#.......#...#
        #.#####.#.#.#########v#
        #.#...#...#...###...>.#
        #.#.#v#######v###.###v#
        #...#.>.#...>.>.#.###.#
        #####v#.#.###v#.#.###.#
        #.....#...#...#.#.#...#
        #.#########.###.#.#.###
        #...###...#...#...#.###
        ###.###.#.###v#####v###
        #...#...#.#.>.>.#.>.###
        #.###.###.#.###.#.#v###
        #.....###...###...#...#
        #####################.#
    """.trimIndent()
