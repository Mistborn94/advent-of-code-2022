package day19

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day19KtTest {

    private val day = 19

    @Test
    fun sample1() {
        val text = """
|Blueprint 1: Each ore robot costs 4 ore. Each clay robot costs 2 ore. Each obsidian robot costs 3 ore and 14 clay. Each geode robot costs 2 ore and 7 obsidian.
|Blueprint 2: Each ore robot costs 2 ore. Each clay robot costs 3 ore. Each obsidian robot costs 3 ore and 8 clay. Each geode robot costs 3 ore and 12 obsidian.
        """.trimMargin().trim()

        assertEquals(33, solveA(text))
        assertEquals(3472, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1346, solveA)

        val solveB = solveB(lines)
        assertEquals(7644, solveB)

        println("B: $solveB")
    }
}