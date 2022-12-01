package day1

fun solveA(text: String): Int {
    return text.split("\n\n")
        .map { it.lines() }
        .maxOf { it.sumOf { line -> line.toInt() } }

}

fun solveB(text: String): Int {
    return text.split("\n\n")
        .asSequence()
        .map {
            it.lines().sumOf { line -> line.toInt() }
        }
        .sortedDescending()
        .take(3)
        .sum()
}
