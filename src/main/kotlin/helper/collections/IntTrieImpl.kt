package helper.collections

import helper.point.HyperspacePoint

/**
 * Stores constant-length integer sequences where each element is within certain bounds
 */
interface IntTrie {

    val lowerBounds: IntArray
    val upperBounds: IntArray

    fun count(): Int
    fun countInArea(area: Array<IntRange>): Int
    fun countWithNeighbours(location: IntArray): Int
    fun add(location: IntArray)
    fun add(location: HyperspacePoint) {
        add(location.parts)
    }

    fun addAll(locations: Collection<HyperspacePoint>) {
        locations.forEach(this::add)
    }

    fun remove(location: IntArray)
    fun removeAll(locations: List<HyperspacePoint>) {
        locations.forEach { this.remove(it.parts) }
    }

    fun contains(location: IntArray): Boolean
    fun contains(location: HyperspacePoint): Boolean = contains(location.parts)
    fun subtree(index: Int): IntTrie?

    companion object {
        /**
         * Creates an IntTrie that can store integer sequences with the same length as the bounds arrays
         * @param lowerBounds the lower bounds for the integers (inclusive)
         * @param upperBounds the upper bounds for the integers (inclusive)
         *
         * The [lowerBounds] and [upperBounds] arrays must have the same length
         */
        fun create(lowerBounds: IntArray, upperBounds: IntArray): IntTrie {
            if (lowerBounds.size != upperBounds.size) {
                throw IllegalArgumentException("Lower and upper bounds arrays must have the same size")
            }
            return TrieNode.InnerNode(lowerBounds, upperBounds, 0)
        }
    }
}

private sealed class TrieNode {

    abstract fun count(): Int
    abstract fun countWithNeighbours(location: IntArray): Int
    abstract fun countInArea(area: Array<IntRange>): Int

    class InnerNode(override val lowerBounds: IntArray, override val upperBounds: IntArray, val dimension: Int) :
        TrieNode(), IntTrie {
        private val lowerBound get() = lowerBounds[dimension]
        private val upperBound get() = upperBounds[dimension]
        private val nodes: Array<TrieNode> = Array(upperBound - lowerBound + 1) { EmptyNode }

        override fun add(location: IntArray) {
            val nodeIndex = location[dimension] - lowerBound

            if (dimension == location.lastIndex) {
                nodes[nodeIndex] = LeafNode
            } else {
                val trieNode = nodes[nodeIndex]
                if (trieNode is InnerNode) {
                    trieNode.add(location)
                } else {
                    val newNode = InnerNode(lowerBounds, upperBounds, dimension + 1)
                    newNode.add(location)
                    nodes[nodeIndex] = newNode
                }
            }
        }

        override fun remove(location: IntArray) {
            val nodeIndex = location[dimension] - lowerBound

            if (dimension == location.lastIndex) {
                nodes[nodeIndex] = EmptyNode
            } else {
                val trieNode = nodes[nodeIndex]
                if (trieNode is InnerNode) {
                    trieNode.remove(location)
                } else {
                    nodes[nodeIndex] = EmptyNode
                }
            }
        }

        override fun contains(location: IntArray): Boolean {
            val nodeIndex = location[dimension] - lowerBound

            if (nodeIndex !in nodes.indices) {
                return false
            }

            return when (val nextNode = nodes[nodeIndex]) {
                is EmptyNode -> false
                is LeafNode -> true
                is InnerNode -> nextNode.contains(location)
            }
        }

        override fun count(): Int = nodes.sumOf { it.count() }
        override fun countInArea(area: Array<IntRange>): Int {
            val lowerIndex = area[dimension].first - lowerBound
            val upperIndex = area[dimension].last - lowerBound
            return nodes.filterIndexed { i, _ -> i in lowerIndex..upperIndex }.sumOf { it.countInArea(area) }
        }

        override fun countWithNeighbours(location: IntArray): Int {
            val nodeIndex = location[dimension] - lowerBound

            return countWithNeighbours(nodeIndex, location) +
                    countWithNeighbours(nodeIndex - 1, location) +
                    countWithNeighbours(nodeIndex + 1, location)
        }

        override fun subtree(index: Int): IntTrie? {
            val nodeIndex = index - lowerBound
            if (nodeIndex !in nodes.indices) {
                return null
            }
            return when (val nextNode = nodes[nodeIndex]) {
                is InnerNode -> nextNode
                else -> null
            }
        }

        private fun countWithNeighbours(nodeIndex: Int, location: IntArray) =
            if (nodeIndex in nodes.indices) {
                nodes[nodeIndex].countWithNeighbours(location)
            } else {
                0
            }
    }


    object EmptyNode : TrieNode() {
        override fun count(): Int = 0
        override fun countWithNeighbours(location: IntArray): Int = 0
        override fun countInArea(area: Array<IntRange>) = 0
    }

    object LeafNode : TrieNode() {
        override fun count(): Int = 1
        override fun countWithNeighbours(location: IntArray) = 1
        override fun countInArea(area: Array<IntRange>): Int = 1
    }
}