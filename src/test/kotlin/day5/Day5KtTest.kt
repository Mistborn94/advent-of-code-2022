package day5

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day5KtTest {

    private val day = 5

    @Test
    fun sample1() {
        val text = """
    [D]    
[N] [C]    
[Z] [M] [P]
 1   2   3 

move 1 from 2 to 1
move 3 from 1 to 3
move 2 from 2 to 1
move 1 from 1 to 2
        """.trimMargin().trimEnd()

        assertEquals("CMZ", solveA(text))
        assertEquals("MCD", solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals("JCMHLVGMG", solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals("LVMRWSSPZ", solveB)
    }
}