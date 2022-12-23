package day23

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

        assertEquals(110, solveA(text, 10))
        assertEquals(20, solveB(text))
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

        assertEquals(25, solveA(text, 10))
        assertEquals(4, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines, 10)
        println("A: $solveA")
        assertEquals(4208, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(1016, solveB)
    }
}