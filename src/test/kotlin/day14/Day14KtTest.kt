package day14

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day14KtTest {

    private val day = 14

    @Test
    fun sample1() {
        val text = """
            |
        """.trimMargin().trim()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    @Ignore
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trim()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trim()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(0, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(0, solveB)
    }
}