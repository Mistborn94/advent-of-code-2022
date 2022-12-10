package day8

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day8KtTest {

    private val day = 8

    @Test
    fun sample1() {
        val text = """
            |30373
            |25512
            |65332
            |33549
            |35390
        """.trimMargin().trimEnd()

        assertEquals(21, solveA(text))
        assertEquals(8, solveB(text))
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
        assertEquals(1676, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(313200, solveB)
    }
}