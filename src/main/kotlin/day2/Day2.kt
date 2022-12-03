package day2

val baseScores = mapOf(
    'X' to 1, //Rock
    'Y' to 2, //Paper
    'Z' to 3  //Scissors
)

val roundScores = mapOf(
    "A X" to 3,
    "A Y" to 6,
    "A Z" to 0,

    "B X" to 0,
    "B Y" to 3,
    "B Z" to 6,

    "C X" to 6,
    "C Y" to 0,
    "C Z" to 3,
)

fun solveA(text: String): Int {
    return text.trim().lines().sumOf { str ->
        val me = str[2]
        baseScores[me]!! + roundScores[str]!!
    }
}

fun solveB(text: String): Int {
    return text.lines().sumOf {
        val op = it[0]

        val roundScore: Int = when (it[2]) {
            'X' -> 0
            'Y' -> 3
            'Z' -> 6
            else -> throw IllegalArgumentException()
        }

        val item = roundScores.firstNotNullOf { (key, value) -> if (key[0] == op && value == roundScore) key else null }
        roundScore + baseScores[item[2]]!!
    }
}