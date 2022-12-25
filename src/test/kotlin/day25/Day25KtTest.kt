package day25

import helper.Debug
import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day25KtTest {

    private val day = 25

    @Test
    fun sample1() {
        val text = """
            |1=-0-2
12111
2=0=
21
2=01
111
20012
112
1=-1=
1-12
12
1=
122
        """.trimMargin().trimEnd()

        assertEquals("2=-1=0", solveA(text, Debug.Enabled))
//        assertEquals(0, solveB(text, Debug.Enabled))
    }

    @Test
    fun testToSnafu() {
        val list = listOf(
            (1 to "1"),
            (2 to "2"),
            (3 to "1="),
            (4 to "1-"),
            (5 to "10"),
            (6 to "11"),
            (7 to "12"),
            (8 to "2="),
            (9 to "2-"),
            (10 to "20"),
            (15 to "1=0"),
            (20 to "1-0"),
            (2022 to "1=11-2"),
            (12345 to "1-0---0"),
            (314159265 to "1121-1110-1=0")
        )

        list.forEach { (int, expected) ->
            assertEquals(expected, toSnafu(int.toLong()), "Wrong snafu number for $int")
        }
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals("2=1-=02-21===-21=200", solveA)
    }
}