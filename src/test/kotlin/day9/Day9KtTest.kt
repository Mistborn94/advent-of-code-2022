package day9

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day9KtTest {

    private val day = 9

    @Test
    fun sample1() {
        val text = """
            |R 4
U 4
L 3
D 1
R 4
D 1
L 5
R 2
        """.trimMargin().trimEnd()

        assertEquals(13, solveA(text))
        assertEquals(1, solveB(text))
    }

    @Test
    fun sample2() {
        val text = """
            |R 5
U 8
L 8
D 3
R 17
D 10
L 25
U 20""".trimMargin().trimEnd()

        assertEquals(36, solveB(text))
    }


    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(6018, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(2619, solveB)
    }
}