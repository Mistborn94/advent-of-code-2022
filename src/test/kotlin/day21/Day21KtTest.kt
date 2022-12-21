package day21

import helper.readDayFile
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day21KtTest {

    private val day = 21

    @Test
    fun sample1() {
        val text = """|root: pppw + sjmn
dbpl: 5
cczh: sllz + lgvd
zczc: 2
ptdq: humn - dvpt
dvpt: 3
lfqf: 4
humn: 5
ljgn: 2
sjmn: drzm * dbpl
sllz: 4
pppw: cczh / lfqf
lgvd: ljgn * ptdq
drzm: hmdt - zczc
hmdt: 32
        """.trimMargin().trimEnd()

        assertEquals(152, solveA(text))
        assertEquals(301, solveB(text))
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
        assertEquals(87457751482938, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(3221245824363, solveB)
    }
}