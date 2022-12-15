package helper

import kotlin.math.max
import kotlin.math.min

class CompositeRange private constructor(private var ranges: List<IntRange>) {

    constructor() : this(emptyList())
    constructor(range: IntRange) : this(listOf(range))

    fun subtract(subtract: IntRange): CompositeRange {
        if (subtract.isEmpty()) {
            return this
        }
        val newRanges = ArrayList<IntRange>(ranges.size + 2)
        ranges.forEach {
            val newEnd = subtract.last + 1
            val newStart = subtract.first - 1
            if (it.contains(newStart) || it.contains(newEnd)) {
                if (it.contains(newStart)) {
                    newRanges.add(it.first..newStart)
                }
                if (it.contains(newEnd)) {
                    newRanges.add(newEnd..it.last)
                }
            } else if (!subtract.contains(it.first) || !subtract.contains(it.last)) {
                newRanges.add(it)
            }
        }
        ranges = newRanges
        return this
    }

    fun add(addition: IntRange): CompositeRange {
        if (addition.isEmpty()) {
            return this
        }
        if (ranges.isEmpty()) {
            ranges = listOf(addition)
        }

        val new = ArrayList<IntRange>(ranges.size)
        val overlap = ArrayList<IntRange>(ranges.size)
        ranges.forEach {
            if (it.overlaps(addition)) {
                overlap.add(it)
            } else {
                new.add(it)
            }
        }
        overlap.add(addition)
        val overlapRange = overlap.minOf { it.first }..overlap.maxOf { it.last }
        new.add(overlapRange)

        ranges = new
        return this
    }

    fun overlap(range: IntRange): CompositeRange {
        val new = ArrayList<IntRange>(ranges.size)
        ranges.forEach {
            val overlap = it.overlap(range)
            if (!overlap.isEmpty()) {
                new.add(it)
            }
        }
        return this;
    }

    fun count() = ranges.sumOf { it.last - it.first + 1 }
    fun first() = ranges.first().first

    override fun toString(): String = ranges.joinToString(prefix = "[", postfix = "]", separator = ",")
    fun isEmpty(): Boolean = ranges.isEmpty()
}

fun <T : Comparable<T>> ClosedRange<T>.overlaps(other: ClosedRange<T>): Boolean {
    return this.start <= other.start && other.start <= this.endInclusive
            || other.start < this.start && this.start <= other.endInclusive
}

fun IntRange.overlap(other: IntRange): IntRange {
    val a = if (this.first < other.first) this else other
    val b = if (this.first < other.first) other else this

    return if (a.last < b.first) {
        IntRange.EMPTY
    } else {
        max(a.first, b.first)..min(a.last, b.last)
    }
}

