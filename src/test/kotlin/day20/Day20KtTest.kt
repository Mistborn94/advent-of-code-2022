package day20

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day20KtTest {

    private val day = 20

    @Test
    fun sample1() {
        val text = """
            |1
            |2
            |-3
            |3
            |-2
            |0
            |4
        """.trimMargin().trimEnd()

        assertEquals(3, solveA(text))
        assertEquals(1623178306, solveB(text))
    }

    @Test
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        assertEquals(-5, solveA(text))
        assertEquals(-5681124071, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(14526, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(9738258246847, solveB)
    }
}