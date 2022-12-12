package day12

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day12KtTest {

    private val day = 12

    @Test
    fun sample1() {
        val text = """
            |Sabqponm
            |abcryxxl
            |accszExk
            |acctuvwj
            |abdefghi
        """.trimMargin().trimEnd()

        assertEquals(31, solveA(text))
        assertEquals(29, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(472, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(465, solveB)
    }
}