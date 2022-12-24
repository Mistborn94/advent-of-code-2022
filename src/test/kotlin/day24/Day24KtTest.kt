package day24

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day24KtTest {

    private val day = 24

    @Test
    fun sample1() {
        val text = """
            |#.######
            |#>>.<^<#
            |#.<..<<#
            |#>v.><>#
            |#<^v^^>#
            |######.#
        """.trimMargin().trimEnd()

        assertEquals(18, solveA(text, Debug.Enabled))
        assertEquals(54, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(245, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(798, solveB)
    }
}