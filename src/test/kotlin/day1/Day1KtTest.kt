package day1

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day1KtTest {

    private val day = 1

    @Test
    fun sample1() {
        val lines = """1000
2000
3000

4000

5000
6000

7000
8000
9000

10000"""

        assertEquals(24000, solveA(lines))
        assertEquals(45000, solveB(lines))
    }


    @Test
    fun solve() {
        val text = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(text)
        println("A: $solveA")
        assertEquals(67633, solveA)

        val solveB = solveB(text)
        println("B: $solveB")
        assertEquals(199628, solveB)
    }
}