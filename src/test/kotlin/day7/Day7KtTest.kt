package day7

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day7KtTest {

    private val day = 7

    @Test
    fun sample2() {
        val text = readDayFile(day, "sample1.in").readText().trimEnd()

        assertEquals(95437, solveA(text))
        assertEquals(24933642, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1307902, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(7068748, solveB)
    }
}