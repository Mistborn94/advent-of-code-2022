package day6

fun solveA(text: String): Int = findMarker(text, 4)
fun solveB(text: String): Int = findMarker(text, 14)

private fun findMarker(text: String, size: Int) = text
    .windowed(size, 1)
    .indexOfFirst { it.toSet().size == size } + size
