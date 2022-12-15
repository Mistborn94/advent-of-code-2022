package helper

class CompositeRange private constructor(private var ranges: List<ClosedRange<Int>>) {

    constructor() : this(emptyList())
    constructor(range: ClosedRange<Int>) : this(listOf(range))

    fun subtract(subtract: ClosedRange<Int>) {
        val newRanges = ArrayList<ClosedRange<Int>>(ranges.size + 2)
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

        val new = ArrayList<ClosedRange<Int>>(ranges.size)
        val overlap = ArrayList<ClosedRange<Int>>(ranges.size)
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
    return this.start <= other.start && other.start <= this.endInclusive
            || other.start < this.start && this.start <= other.endInclusive
}

