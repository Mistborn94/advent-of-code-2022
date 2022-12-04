package day3

fun solveA(text: String): Int {
    return text.trim().lines().sumOf { line ->
        val comp1 = line.substring(0, line.length / 2)
        val comp2 = line.substring(line.length / 2)
        getScore(comp1.first { it in comp2 })
    }
}

private fun getScore(c: Char) = if (c.isLowerCase()) {
    c.code - 'a'.code + 1
} else {
    c.code - 'A'.code + 27
}

fun solveB(text: String): Int {
    val groups = text.trim().lines().chunked(3)
    return groups.sumOf { (a, b, c) ->
        getScore(a.first { it in b && it in c })
    }
}
