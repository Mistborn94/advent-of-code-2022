package day4

fun solveA(text: String): Int {
    return parseRanges(text)
        .count { (range1, range2) -> range1 in range2 || range2 in range1 }
}

fun solveB(text: String): Int {
    return parseRanges(text)
        .count { (range1, range2) -> range2.overlaps(range1) }
}

operator fun IntRange.contains(other: IntRange) = this.contains(other.first) && this.contains(other.last)
fun IntRange.overlaps(other: IntRange) = this.contains(other.first) || this.contains(other.last)
        || other.contains(this.first) || other.contains(this.last)

private fun parseRanges(text: String) = text.lines()
    .map { it.split(",", "-") }
    .map { (a, b, c, d) ->
        val range1 = a.toInt()..b.toInt()
        val range2 = c.toInt()..d.toInt()
        range1 to range2
    }
