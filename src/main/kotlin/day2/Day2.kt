package day2

val rock = setOf("A", "X")
val paper = setOf("B", "Y")
val scis = setOf("C", "Z")
fun solveA(text: String): Int {

    return text.trim().lines().map { it.split(" ") }.sumOf { (op, me) ->
        scoreA(op, me)
    }
    //rock = 1 A X
    //paper = 2 B Y
    //scissors = 3 C Z
    return 0;
}

private fun scoreA(op: String, me: String) = when (op) {
    in rock -> {
        when (me) {
            in rock -> 3 + 1
            in paper -> 6 + 2
            in scis -> 0 + 3
            else -> throw IllegalArgumentException()
        }
    }

    in paper -> {
        when (me) {
            in rock -> 0 + 1
            in paper -> 3 + 2
            in scis -> 6 + 3
            else -> throw IllegalArgumentException()
        }
    }

    in scis -> {
        when (me) {
            in rock -> 6 + 1
            in paper -> 0 + 2
            in scis -> 3 + 3
            else -> throw IllegalArgumentException("me " + me)
        }
    }

    else -> throw IllegalArgumentException("op " + op)
}


fun solveB(text: String): Int {
    return text.trim().lines().map { it.split(" ") }.sumOf { (op, me) ->
        scoreB(op, me)
    }
    return 0
}

private fun scoreB(op: String, me: String) = when (op) {
    in rock -> {
        when (me) {
            "X" -> 0 + 3
            "Y" -> 3 + 1
            "Z" -> 6 + 2
            else -> throw IllegalArgumentException()
        }
    }

    in paper -> {
        when (me) {
            "X" -> 0 + 1
            "Y" -> 3 + 2
            "Z" -> 6 + 3
            else -> throw IllegalArgumentException()
        }
    }

    in scis -> {
        when (me) {
            "X" -> 0 + 2
            "Y" -> 3 + 3
            "Z" -> 6 + 1
            else -> throw IllegalArgumentException()
        }
    }

    else -> throw IllegalArgumentException("op " + op)
}
