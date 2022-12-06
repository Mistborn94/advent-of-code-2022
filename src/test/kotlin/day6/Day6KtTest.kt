package day6

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day6KtTest {

    private val day = 6

    @Test
    fun sample1() {
        val text = """mjqjpqmgbljsphdztnvjfqwrcgsmlb""".trimMargin().trimEnd()

        assertEquals(7, solveA(text))
        assertEquals(19, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1235, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(3051, solveB)
    }
}