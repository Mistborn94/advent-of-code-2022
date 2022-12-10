package day10

import helper.readDayFile
import kotlin.test.Test
import kotlin.test.assertEquals


internal class Day10KtTest {

    private val day = 10

    @Test
    fun sample1() {
        val text = readDayFile(day, "sample1.in").readText().trimEnd()

        assertEquals(13140, solveA(text))
        val solveB = solveB(text)
        val answB = """
        |##  ##  ##  ##  ##  ##  ##  ##  ##  ##  
        |###   ###   ###   ###   ###   ###   ### 
        |####    ####    ####    ####    ####    
        |#####     #####     #####     #####     
        |######      ######      ######      ####
        |#######       #######       #######     """.trimMargin()
        assertEquals(answB, solveB)
    }

    @Test
    fun solve() {
        val lines = readDayFile(day, "input").readText().trimEnd()

        val solveA = solveA(lines)
        println("A: $solveA")
        assertEquals(14320, solveA)

        val answB = """
        |###   ##  ###  ###  #  #  ##  ###    ## 
        |#  # #  # #  # #  # # #  #  # #  #    # 
        |#  # #    #  # ###  ##   #  # #  #    # 
        |###  #    ###  #  # # #  #### ###     # 
        |#    #  # #    #  # # #  #  # #    #  # 
        |#     ##  #    ###  #  # #  # #     ##  """.trimMargin()

        val solveB = solveB(lines)
        println("B:\n$solveB")
        assertEquals(answB, solveB)
    }
}