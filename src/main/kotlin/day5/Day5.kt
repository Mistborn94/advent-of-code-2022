package day5

import java.util.*

val pattern = "move (\\d+) from (\\d+) to (\\d+)".toRegex()

fun solveA(text: String): String = solve(text) { stacks, count, from, to ->
    repeat(count) {
        val pop = stacks[from - 1].pop()
        stacks[to - 1].push(pop)
    }
}

fun solveB(text: String): String = solve(text) { stacks, count, from, to ->
    val pop = (0 until count).map { stacks[from - 1].pop() }
    stacks[to - 1].addAll(pop.reversed())
}

private fun solve(text: String, handler: (List<Stack<Char>>, Int, Int, Int) -> Unit): String {
    val (start, instr) = text.split("\n\n")

    val stacks = start.lines().last().chunked(4).map<String, Stack<Char>> { Stack() }

    start.lines()
        .dropLast(1)
        .reversed()
        .forEach { line ->
            line.chunked(4).forEachIndexed { index, crate ->
                if (crate[1] != ' ') {
                    stacks[index].push(crate[1])
                }
            }
        }

    instr.lines().forEach {
        val matchResult = pattern.matchEntire(it)
        val (count, from, to) = matchResult!!.groupValues.drop(1).map(String::toInt)

        handler(stacks, count, from, to)
    }
    return stacks.map { it.peek() }.joinToString(separator = "")
}


