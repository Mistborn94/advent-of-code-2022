package day6

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day6KtTest {

    private val day = 6

    @Test
    fun sample1() {
        val text = """
            |
        """.trimMargin().trimEnd()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    @Ignore
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(0, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(0, solveB)
    }
}