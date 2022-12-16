package day16

import helper.cartesianProduct
import helper.graph.findShortestPath

val regex = """Valve ([A-Z]+) has flow rate=(\d+); tunnels? leads? to valves? ([A-Z, ]+)""".toRegex()

//What is the most pressure you can release? in 30 minutes
fun solveA(text: String): Int {
    val day16 = parseInput(text)
    return day16.solveA().score
}

fun solveB(text: String): Int {
    val day16 = parseInput(text)
    return day16.solveB()
}

private const val STARTING_ROOM = "AA"

private fun parseInput(text: String): Day16 {
    val valves = mutableMapOf<String, Int>()
    val rooms = text.lines().associate { line ->
        val (_, name, rate, neighbours) = regex.matchEntire(line)!!.groupValues
        val intRate = rate.toInt()
        if (intRate > 0) {
            valves[name] = intRate
        }
        name to Room(name, neighbours.split(", "))
    }

    val valveRoomDistances = (valves.keys + STARTING_ROOM).cartesianProduct(valves.keys).associateWith { (a, b) ->
        findShortestPath(a, b, { rooms[it]!!.neighbours() }).getScore()
    }

    return Day16(valveRoomDistances, valves)
}

val TIME_EXCEEDED_A = Result(-1, 0, -1)

private const val LIMIT_A = 30
private const val LIMIT_B = 26

class Day16(private val roomDistances: Map<Pair<String, String>, Int>, private val valves: Map<String, Int>) {

    fun solveA(currentRoom: String = STARTING_ROOM, openedValves: Set<String> = emptySet(), time: Int = 1, pressurePerMinute: Int = 0, score: Int = 0): Result {
        val remainingValves = valves.keys.filterTo(mutableSetOf()) { it !in openedValves }
        return if (remainingValves.isEmpty()) {
            Result(time, pressurePerMinute, score).adjustToA()
        } else {
            val max = remainingValves.maxOf { nextRoom ->
                val movingTime = roomDistances[currentRoom to nextRoom]!!
                val newTime = time + movingTime + 1
                if (newTime > LIMIT_A) {
                    TIME_EXCEEDED_A
                } else {
                    val newPressure = pressurePerMinute + valves[nextRoom]!!
                    val newScore = score + pressurePerMinute * (movingTime + 1) + valves[nextRoom]!!
                    solveA(nextRoom, openedValves + nextRoom, newTime, newPressure, newScore)
                }
            }

            if (max == TIME_EXCEEDED_A) {
                Result(time, pressurePerMinute, score).adjustToA()
            } else {
                max
            }
        }
    }

    fun solveB(
        openedValves: Set<String> = emptySet(),

        myRoom: String = STARTING_ROOM,
        myTime: Int = 1,
        myPressure: Int = 0,
        myScore: Int = 0,

        elephantRoom: String = STARTING_ROOM,
        elephantTime: Int = 1,
        elephantPressure: Int = 0,
        elephantScore: Int = 0
    ): Int {
        val remainingValves = valves.keys.filterTo(mutableSetOf()) { it !in openedValves }
        return if (remainingValves.isEmpty()) {
            adjustScoreForB(myTime, myScore, myPressure) + adjustScoreForB(elephantTime, elephantScore, elephantPressure)
        } else {
            if (openedValves.isEmpty()) {
                println("Starting first iteration")
                remainingValves.parallelStream()
                    .mapToInt { nextRoom ->
                        val score = getScore(
                            nextRoom, openedValves,
                            myRoom, myTime, myPressure, myScore,
                            elephantRoom, elephantTime, elephantPressure, elephantScore
                        )
                        println("Outer level $nextRoom complete: Score is $score")
                        score
                    }
                    .max()
                    .asInt
            } else {
                remainingValves.maxOf { nextRoom ->
                    getScore(
                        nextRoom, openedValves,
                        myRoom, myTime, myPressure, myScore,
                        elephantRoom, elephantTime, elephantPressure, elephantScore
                    )
                }
            }
        }
    }

    private fun getScore(
        nextRoom: String, openedValves: Set<String>,
        myRoom: String, myCurrentTime: Int, myCurrentPressure: Int, myCurrentScore: Int,
        elephantRoom: String, elephantCurrentTime: Int, elephantCurrentPressure: Int, elephantCurrentScore: Int
    ): Int {
        val newOpened = openedValves + nextRoom

        val myMovingTime = roomDistances[myRoom to nextRoom]!!
        val myNewTime = myCurrentTime + myMovingTime + 1

        val myChoice = if (myNewTime <= LIMIT_B) {
            val myNewPressure: Int = myCurrentPressure + valves[nextRoom]!!
            val myNewScore: Int = myCurrentScore + myCurrentPressure * (myMovingTime + 1) + valves[nextRoom]!!

            solveB(
                newOpened,
                nextRoom, myNewTime, myNewPressure, myNewScore,
                elephantRoom, elephantCurrentTime, elephantCurrentPressure, elephantCurrentScore
            )
        } else null

        val elephantMovingTime = roomDistances[elephantRoom to nextRoom]!!
        val elephantNewTime = elephantCurrentTime + elephantMovingTime + 1

        val elephantChoice = if (elephantNewTime <= LIMIT_B && (elephantRoom != myRoom || myChoice == null)) {
            val elephantNewPressure: Int = elephantCurrentPressure + valves[nextRoom]!!
            val elephantNewScore: Int = elephantCurrentScore + elephantCurrentPressure * (elephantMovingTime + 1) + valves[nextRoom]!!

            solveB(
                newOpened,
                myRoom, myCurrentTime, myCurrentPressure, myCurrentScore,
                nextRoom, elephantNewTime, elephantNewPressure, elephantNewScore
            )
        } else null

        return when {
            myChoice != null && elephantChoice != null -> maxOf(myChoice, elephantChoice)
            myChoice == null && elephantChoice == null -> adjustScoreForB(myCurrentTime, myCurrentScore, myCurrentPressure) + adjustScoreForB(elephantCurrentTime, elephantCurrentScore, elephantCurrentPressure)
            myChoice != null -> myChoice
            elephantChoice != null -> elephantChoice
            else -> throw IllegalStateException("Thrown")
        }
    }

    private fun adjustScoreForB(time: Int, score: Int, pressurePerMinute: Int): Int {
        return score + (LIMIT_B - time) * pressurePerMinute
    }
}

class Result(val time: Int, val pressurePerMinute: Int, val score: Int) : Comparable<Result?> {
    override fun compareTo(other: Result?): Int = if (other == null) -1 else score.compareTo(other.score)

    private fun adjustTo(target: Int): Result {
        return if (target == time) {
            this
        } else {
            val remainingMinutes = target - time
            Result(target, pressurePerMinute, score + remainingMinutes * pressurePerMinute)
        }
    }

    fun adjustToA() = adjustTo(LIMIT_A)
}

data class Room(val name: String, val neighbours: List<String>) {
    fun neighbours() = neighbours
}