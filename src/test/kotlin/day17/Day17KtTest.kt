package day17

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day17KtTest {

    private val day = 17

    @Test
    fun sample1() {
        val text = """
            |>>><<><>><<<>><>>><<<>>><<<><<<>><>><<>>
        """.trimMargin().trim()

        val solveA = solveA(text)
        val solveAWithB = solveB(text, 2022).map { it.toInt() }
        assertEquals(3068, solveA.max())
        assertEquals(solveA, solveAWithB)
        assertEquals(1514285714288, solveB(text).max())
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(3161, solveA.max())

        val solveB = solveB(lines).max()
        println("B: $solveB")
        assertEquals(1575931232076, solveB)
    }
}