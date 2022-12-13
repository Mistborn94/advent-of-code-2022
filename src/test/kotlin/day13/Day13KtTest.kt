package day13

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day13KtTest {

    private val day = 13

    @Test
    fun sample1() {
        val text = """
            |[1,1,3,1,1]
[1,1,5,1,1]

[[1],[2,3,4]]
[[1],4]

[9]
[[8,7,6]]

[[4,4],4,4]
[[4,4],4,4,4]

[7,7,7,7]
[7,7,7]

[]
[3]

[[[]]]
[[]]

[1,[2,[3,[4,[5,6,7]]]],8,9]
[1,[2,[3,[4,[5,6,0]]]],8,9]
        """.trimMargin().trim()

        assertEquals(13, solveA(text))
        assertEquals(140, solveB(text))
    }

    @Test
    @Ignore
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(5825, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(24477, solveB)
    }
}