package day2

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day2KtTest {

    private val day = 2

    @Test
    fun sample1() {
        val text = """
A Y
B X
C Z
        """.trimMargin()

        assertEquals(15, solveA(text))
        assertEquals(12, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(8890, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(10238, solveB)
    }
}