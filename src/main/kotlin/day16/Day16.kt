package day16

import helper.graph.findShortestPath
import kotlin.math.max

val regex = """Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)""".toRegex()

//What is the most pressure you can release? in 30 minutes
fun solveA(text: String): Int {
    val day16 = parseInput(text)
    return day16.solveAB(LIMIT_A, false)
}

fun solveB(text: String): Int {
    val day16 = parseInput(text)
    return day16.solveAB(LIMIT_B, true)
}

private const val STARTING_ROOM = "AA"

private fun parseInput(text: String): Day16 {
    val valves = mutableMapOf<String, Int>()
    val rooms = mutableMapOf<String, List<String>>()

    text.lines().forEach { line ->
        val (_, name, rate, neighbours) = regex.matchEntire(line)!!.groupValues
        val intRate = rate.toInt()
        if (intRate > 0) {
            valves[name] = intRate
        }
        rooms[name] = neighbours.split(", ")
    }

    val valveRoomDistances = (valves.keys + STARTING_ROOM).associateWith { a ->
        valves.keys.associateWith { b -> findShortestPath(a, b, { rooms[it]!! })!!.getScore() }
    }

    return Day16(valveRoomDistances, valves)
}

private const val LIMIT_A = 30
private const val LIMIT_B = 26

class Day16(private val roomDistances: Map<String, Map<String, Int>>, private val valves: Map<String, Int>) {

    fun solveAB(limit: Int, doElephant: Boolean, currentRoom: String = STARTING_ROOM, openedValves: Set<String> = emptySet(), time: Int = 1): Int {
        val remainingValves = valves.keys.filterTo(mutableSetOf()) { it !in openedValves }
        return if (remainingValves.isEmpty()) {
            0
        } else {
            val currentRoomDistances = roomDistances[currentRoom]!!
            //I can take the next room...
            val maxOfRooms = remainingValves.maxOf { nextRoom ->
                val movingTime = currentRoomDistances[nextRoom]!!
                val newTime = time + movingTime + 1
                if (newTime > limit) {
                    0
                } else {
                    val flowRate = valves[nextRoom]!!
                    val newScore = (limit - newTime + 1) * flowRate
                    newScore + solveAB(limit, doElephant, nextRoom, openedValves + nextRoom, newTime)
                }
            }

            //Or the elephant can take it from here
            if (doElephant) {
                max(maxOfRooms, solveAB(limit, false, STARTING_ROOM, openedValves))
            } else {
                maxOfRooms
            }
        }
    }

}

data class Room(val name: String, val neighbours: List<String>) {
    fun neighbours() = neighbours
}