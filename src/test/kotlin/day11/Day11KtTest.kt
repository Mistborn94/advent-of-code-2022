package day11

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day11KtTest {

    private val day = 11

    @Test
    fun sample1() {
        val text = """
Monkey 0:
  Starting items: 79, 98
  Operation: new = old * 19
  Test: divisible by 23
    If true: throw to monkey 2
    If false: throw to monkey 3

Monkey 1:
  Starting items: 54, 65, 75, 74
  Operation: new = old + 6
  Test: divisible by 19
    If true: throw to monkey 2
    If false: throw to monkey 0

Monkey 2:
  Starting items: 79, 60, 97
  Operation: new = old * old
  Test: divisible by 13
    If true: throw to monkey 1
    If false: throw to monkey 3

Monkey 3:
  Starting items: 74
  Operation: new = old + 3
  Test: divisible by 17
    If true: throw to monkey 0
    If false: throw to monkey 1
        """.trim()

        assertEquals(10605, solveA(text))
        assertEquals(2713310158, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(110264, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(23612457316, solveB)
    }
}