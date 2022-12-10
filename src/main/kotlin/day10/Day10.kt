package day10

import kotlin.math.abs

//Find the signal strength during the 20th, 60th, 100th, 140th, 180th, and 220th cycles.
// What is the sum of these six signal strengths?

fun solveA(text: String): Long {
    val list = mutableListOf<Long>()
    var x = 1

    text.lines().forEach { line ->
        list.add(x.toLong())
        if (line.startsWith("addx")) {
            val (_, inc) = line.split(" ")
            x += inc.toInt()
            list.add(x.toLong())
        }
    }
    val relevant = setOf(20, 60, 100, 140, 180, 220)
    return relevant.sumOf { sigStrength(list, it) }
}

//The i-2 solves for 'during' instead of 'after'
private fun sigStrength(list: MutableList<Long>, i: Int) = list[i - 2] * i

//CRT: 40 wide and 6 high.
//Vertical position of the sprite doesn't matter
fun solveB(text: String): String {
    var sprite = 1
    var cycle = 0

    val screen = mutableListOf<Char>()

    text.lines().forEach { line ->
        screen.add(draw(cycle, sprite))
        if (line.startsWith("noop")) {
            cycle += 1
        } else if (line.startsWith("addx")) {
            screen.add(draw(cycle + 1, sprite))
            val (_, inc) = line.split(" ")
            sprite += inc.toInt()
            cycle += 2

        }
    }

    return screen.chunked(40).joinToString(separator = "\n") { it.joinToString(separator = "") }
}

fun draw(cycle: Int, sprite: Int): Char {
    val crtX = cycle % 40

    return if (abs(crtX - sprite) <= 1) {
        '#'
    } else {
        ' '
    }
}
