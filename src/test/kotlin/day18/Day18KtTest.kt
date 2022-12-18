package day18

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day18KtTest {

    private val day = 18

    @Test
    fun sample1() {
        val text = """
1,1,1
2,1,1
        """.trimMargin().trimEnd()

        assertEquals(10, solveA(text))
        assertEquals(10, solveB(text))
    }

    @Test
    fun sample2() {
        val text = """
2,2,2
1,2,2
3,2,2
2,1,2
2,3,2
2,2,1
2,2,3
2,2,4
2,2,6
1,2,5
3,2,5
2,1,5
2,3,5
        """.trimMargin().trimEnd()

        assertEquals(64, solveA(text))
        assertEquals(58, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(4320, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(2456, solveB)
    }
}