package helper

import java.util.*

class CompositeRange(private var ranges: List<ClosedRange<Int>>) {

    fun subtract(subtract: ClosedRange<Int>) {
        val newRanges = LinkedList<ClosedRange<Int>>()
        ranges.forEach {
            val newEnd = subtract.endInclusive + 1
            val newStart = subtract.start - 1
            if (it.contains(newStart) || it.contains(newEnd)) {
                if (it.contains(newStart)) {
                    newRanges.add(it.start..newStart)
                }
                if (it.contains(newEnd)) {
                    newRanges.add(newEnd..it.endInclusive)
                }
            } else if (!subtract.contains(it.start) || !subtract.contains(it.endInclusive)) {
                newRanges.add(it)
            }
        }
        ranges = newRanges
    }

    fun add(addition: ClosedRange<Int>) {
        if (ranges.isEmpty()) {
            ranges = listOf(addition)
        }

        val new = LinkedList<ClosedRange<Int>>()
        val overlap = LinkedList<ClosedRange<Int>>()
        ranges.forEach {
            if (it.overlaps(addition)) {
                overlap.add(it)
            } else {
                new.add(it)
            }
        }
        overlap.add(addition)
        val overlapRange = overlap.minOf { it.start }..overlap.maxOf { it.endInclusive }
        new.add(overlapRange)

        ranges = new
    }

    fun count() = ranges.sumOf { it.endInclusive - it.start + 1 }
    fun first() = ranges.first().start

    override fun toString(): String = ranges.joinToString(",")
}

fun <T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
    val first = if (this.start < other.start) this else other
    val second = if (this.start < other.start) other else this
    return second.start <= first.endInclusive
}

