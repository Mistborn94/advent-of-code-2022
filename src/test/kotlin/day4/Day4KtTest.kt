package day4

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day4KtTest {

    private val day = 4

    @Test
    fun sample1() {
        val text = """
            |2-4,6-8
            |2-3,4-5
            |5-7,7-9
            |2-8,3-7
            |6-6,4-6
            |2-6,4-8
        """.trimMargin().trimEnd()

        assertEquals(2, solveA(text))
        assertEquals(4, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(441, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(861, solveB)
    }
}