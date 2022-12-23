package day23

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals

internal class Day23KtTest {

    private val day = 23

    @Test
    fun sample1() {
        val text = """
            |....#..
            |..###.#
            |#...#.#
            |.#...##
            |#.###..
            |##.#.##
            |.#..#..
        """.trimMargin().trimEnd()

        assertEquals(110, solveA(text, 10, Debug.Enabled))
        assertEquals(20, solveB(text, Debug.Enabled))
    }

    @Test
    fun sample2() {
        val text = """
            |.....
            |..##.
            |..#..
            |.....
            |..##.
            |.....
        """.trimMargin().trimEnd()

        assertEquals(25, solveA(text, 10, Debug.Enabled))
        assertEquals(4, solveB(text, Debug.Enabled))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines, 10, Debug.Disabled)
        println("A: $solveA")
        assertEquals(4208, solveA)

        val solveB = solveB(lines, Debug.Disabled)
        println("B: $solveB")
        assertEquals(1016, solveB)
    }
}