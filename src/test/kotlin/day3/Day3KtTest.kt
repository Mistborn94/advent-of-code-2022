package day3

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day3KtTest {

    private val day = 3

    @Test
    fun sample1() {
        val text = """
            |vJrwpWtwJgWrhcsFMMfFFhFp
            |jqHRNqRjqzjGDLGLrsFMfFZSrLrFZsSL
            |PmmdzqPrVvPwwTWBwg
            |wMqvLMZHhHMvwLHjbvcjnnSBnvTQFn
            |ttgJtRGJQctTZtZT
            |CrZsJsPPZsGzwwsLwLmpwMDw
        """.trimMargin()

        assertEquals(157, solveA(text))
        assertEquals(70, solveB(text))
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(7845, solveA)

        val solveB = solveB(lines)
        println("B: $solveB")
        assertEquals(2790, solveB)
    }
}