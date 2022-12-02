package day2

enum class RpsAction(val baseScore: Int) {
    ROCK(1) {
        override fun losesAgainst(): RpsAction = PAPER
        override fun winsAgainst(): RpsAction = SCISSORS
    },
    PAPER(2) {
        override fun losesAgainst(): RpsAction = SCISSORS
        override fun winsAgainst(): RpsAction = ROCK
    },
    SCISSORS(3) {
        override fun losesAgainst(): RpsAction = ROCK
        override fun winsAgainst(): RpsAction = PAPER
    };

    abstract fun losesAgainst(): RpsAction
    abstract fun winsAgainst(): RpsAction
}
fun solveA(text: String): Int {
    //rock = 1 A X
    //paper = 2 B Y
    //scissors = 3 C Z
    return text.trim().lines().map { it.split(" ") }.sumOf { (op, me) ->
        val opAction = when (op) {
            "A" -> RpsAction.ROCK
            "B" -> RpsAction.PAPER
            "C" -> RpsAction.SCISSORS
            else -> throw IllegalArgumentException();
        }
        val meAction = when (me) {
            "X" -> RpsAction.ROCK
            "Y" -> RpsAction.PAPER
            "Z" -> RpsAction.SCISSORS
            else -> throw IllegalArgumentException();
        }
        val outcomeScore = when (opAction) {
            meAction.winsAgainst() -> 6
            meAction -> 3
            else -> 0
        }
        meAction.baseScore + outcomeScore
    }
}


fun solveB(text: String): Int {
    return text.trim().lines().map { it.split(" ") }.sumOf { (op, me) ->
        val opAction = when (op) {
            "A" -> RpsAction.ROCK
            "B" -> RpsAction.PAPER
            "C" -> RpsAction.SCISSORS
            else -> throw IllegalArgumentException();
        }

        when (me) {
            "X" -> 0 + opAction.winsAgainst().baseScore
            "Y" -> 3 + opAction.baseScore
            "Z" -> 6 + opAction.losesAgainst().baseScore
            else -> throw IllegalArgumentException()
        }
    }
}

