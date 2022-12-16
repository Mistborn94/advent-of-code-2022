package day16

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day16KtTest {

    private val day = 16

    @Test
    fun sample1() {
        val text = """
            |Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
Valve BB has flow rate=13; tunnels lead to valves CC, AA
Valve CC has flow rate=2; tunnels lead to valves DD, BB
Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
Valve EE has flow rate=3; tunnels lead to valves FF, DD
Valve FF has flow rate=0; tunnels lead to valves EE, GG
Valve GG has flow rate=0; tunnels lead to valves FF, HH
Valve HH has flow rate=22; tunnel leads to valve GG
Valve II has flow rate=0; tunnels lead to valves AA, JJ
Valve JJ has flow rate=21; tunnel leads to valve II
        """.trimMargin().trim()

        assertEquals(1651, solveA(text))
        assertEquals(1707, solveB(text))
    }

    @Test
    @Ignore
    fun sample2() {
        val text = readDayFile(day, "sample2.in").readText().trimEnd()

        assertEquals(0, solveA(text))
        assertEquals(0, solveB(text))
    }

    @Test
    @Ignore
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(1474, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(0, solveB)
    }
}