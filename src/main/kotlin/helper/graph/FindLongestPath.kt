package helper.graph

import java.util.*

fun <T : LongestPathNode<T, K>, K> findLongestPathInTime(
    start: T,
    endTime: Int
): T {
    val toVisit = PriorityQueue(Comparator<T> { o1, o2 -> o2.score.compareTo(o1.score) })
    toVisit.add(start)

    val seenConfigurations = mutableMapOf(start.cacheKey() to mutableSetOf(start))
    val nextGeneration = mutableMapOf<K, MutableSet<T>>()

    var currentTime = 0
    while (currentTime < endTime) {
        if (toVisit.isEmpty()) {
            currentTime += 1
            nextGeneration.forEach { (_, values) ->
                toVisit.addAll(values)
            }
            nextGeneration.clear()
        } else {
            val visit = toVisit.remove()
            val neighbours = visit.neighbours()
            neighbours.forEach { neighbour ->
                val cacheKey = neighbour.cacheKey()
                val allSeen = seenConfigurations.getOrPut(cacheKey) { mutableSetOf() }
                val nextGenSeen = nextGeneration.getOrPut(cacheKey) { mutableSetOf() }

                if (allSeen.none { it.canReplace(neighbour) }) {
                    nextGenSeen.removeIf { neighbour.canReplace(it) }
                    nextGenSeen.add(neighbour)
                    allSeen.removeIf { neighbour.canReplace(it) }
                    allSeen.add(neighbour)
                }
            }
        }
    }

    return toVisit.remove()
}

interface LongestPathNode<T : LongestPathNode<T, K>, K> {
    val score: Int

    fun neighbours(): Iterable<T>
    fun cacheKey(): K
    fun canReplace(other: T): Boolean
}