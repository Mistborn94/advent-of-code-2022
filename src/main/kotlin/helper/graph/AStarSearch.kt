package helper.graph

import java.util.*

typealias NeighbourFunction<K> = (K) -> Iterable<K>
typealias CostFunction<K> = (K, K) -> Int
typealias HeuristicFunction<K> = (K) -> Int

/**
 * Implements A* search to find the shortest path between two vertices
 */
fun <K> findShortestPath(
    start: K,
    end: K,
    neighbours: NeighbourFunction<K>,
    cost: CostFunction<K> = { _, _ -> 1 },
    heuristic: HeuristicFunction<K> = { 0 }
): GraphSearchResult<K> = findShortestPathByPredicate(start, { it == end }, neighbours, cost, heuristic)

/**
 * Implements A* search to find the shortest path between two vertices using a predicate to determine the ending vertex
 */
fun <K> findShortestPathByPredicate(
    start: K,
    endFunction: (K) -> Boolean,
    neighbours: NeighbourFunction<K>,
    cost: CostFunction<K> = { _, _ -> 1 },
    heuristic: HeuristicFunction<K> = { 0 }
): GraphSearchResult<K> {
    val toVisit = PriorityQueue(listOf(ScoredVertex(start, 0, heuristic(start))))
    var endVertex: K? = null
    val seenPoints: MutableMap<K, SeenVertex<K>> = mutableMapOf(start to SeenVertex(0, null))

    while (endVertex == null) {
        if (toVisit.isEmpty()) {
            return GraphSearchResult(start, null, seenPoints)
        }

        val (currentVertex, currentScore) = toVisit.remove()
        endVertex = if (endFunction(currentVertex)) currentVertex else null

        val nextPoints = neighbours(currentVertex)
            .filter { it !in seenPoints }
            .map { next -> ScoredVertex(next, currentScore + cost(currentVertex, next), heuristic(next)) }

        toVisit.addAll(nextPoints)
        seenPoints.putAll(nextPoints.associate { it.vertex to SeenVertex(it.score, currentVertex) })
    }

    return GraphSearchResult(start, endVertex, seenPoints)
}

class GraphSearchResult<K>(val start: K, val end: K?, private val result: Map<K, SeenVertex<K>>) {
    fun getScore(vertex: K) = result[vertex]?.cost ?: throw IllegalStateException("Result for $vertex not available")
    fun getScore() = end?.let { getScore(it) } ?: throw IllegalStateException("No path found")

    fun getPath() = end?.let { getPath(it, emptyList()) } ?: throw IllegalStateException("No path found")
    fun seen(): Set<K> = result.keys
    fun end(): K = end ?: throw IllegalStateException("No path found")

    private tailrec fun getPath(endVertex: K, pathEnd: List<K>): List<K> {
        val previous = result[endVertex]?.prev

        return if (previous == null) {
            listOf(endVertex) + pathEnd
        } else {
            getPath(previous, listOf(endVertex) + pathEnd)
        }
    }
}

data class SeenVertex<K>(val cost: Int, val prev: K?)

data class ScoredVertex<K>(val vertex: K, val score: Int, val heuristic: Int) : Comparable<ScoredVertex<K>> {
    override fun compareTo(other: ScoredVertex<K>): Int = (score + heuristic).compareTo(other.score + other.heuristic)
}
