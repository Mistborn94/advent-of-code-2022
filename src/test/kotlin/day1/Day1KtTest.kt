package day1

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day1KtTest {

    private val day = 1

    @Test
    fun sample1() {
        val lines = """|199
                       |200
                       |208
                       |210
                       |200
                       |207
                       |240
                       |269
                       |260
                       |263""".trimMargin()

        assertEquals(7, solveA(lines))
        assertEquals(5, solveB(lines))
    }


    @Test
    fun solve() {
        val text = readDayFile(day, "input").readText().trim()

        val solveA = solveA(text)
        println("A: $solveA")
        assertEquals(1139, solveA)

        val solveB = solveB(text)
        println("B: $solveB")
        assertEquals(1103, solveB)
    }
}