package day14

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day14KtTest {

    private val day = 14

    @Test
    fun sample1() {
        val text = """
            |498,4 -> 498,6 -> 496,6
            |503,4 -> 502,4 -> 502,9 -> 494,9
        """.trimMargin().trim()

        assertEquals(24, solveA(text))
        assertEquals(93, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(979, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(29044, solveB)
    }
}