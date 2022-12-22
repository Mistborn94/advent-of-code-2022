package day22

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day22KtTest {

    private val day = 22

    @Test
    fun sample1() {
        val text = """
            |        ...#
            |        .#..
            |        #...      
            |        ....
            |...#.......#
            |........#...
            |..#....#....
            |..........#.
            |        ...#....
            |        .....#..
            |        .#......
            |        ......#.
            |
            |10R5L5R10L4R5L5
        """.trimMargin().trimEnd()

        assertEquals(6032, solveA(text))
        assertEquals(5031, solveB(text, 4, "sample"))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(80392, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(19534, solveB)
    }
}