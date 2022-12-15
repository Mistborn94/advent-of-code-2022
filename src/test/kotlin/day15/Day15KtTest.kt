package day15

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day15KtTest {

    private val day = 15

    @Test
    fun sample1() {
        val text = """
            |Sensor at x=2, y=18: closest beacon is at x=-2, y=15
            |Sensor at x=9, y=16: closest beacon is at x=10, y=16
            |Sensor at x=13, y=2: closest beacon is at x=15, y=3
            |Sensor at x=12, y=14: closest beacon is at x=10, y=16
            |Sensor at x=10, y=20: closest beacon is at x=10, y=16
            |Sensor at x=14, y=17: closest beacon is at x=10, y=16
            |Sensor at x=8, y=7: closest beacon is at x=2, y=10
            |Sensor at x=2, y=0: closest beacon is at x=2, y=10
            |Sensor at x=0, y=11: closest beacon is at x=2, y=10
            |Sensor at x=20, y=14: closest beacon is at x=25, y=17
            |Sensor at x=17, y=20: closest beacon is at x=21, y=22
            |Sensor at x=16, y=7: closest beacon is at x=15, y=3
            |Sensor at x=14, y=3: closest beacon is at x=15, y=3
            |Sensor at x=20, y=1: closest beacon is at x=15, y=3
        """.trimMargin().trimEnd()

        assertEquals(26, solveA(text, 10))
        assertEquals(56000011, solveB3(text, 20))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(4961647, solveA)

        val solveB = solveB3(lines)
        println("B: $solveB")
        assertEquals(12274327017867, solveB)
    }
}