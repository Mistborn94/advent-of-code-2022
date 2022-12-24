package day24

import helper.Debug
import helper.graph.GraphSearchResult
import helper.graph.findShortestPathByPredicate
import helper.point.*

val directionPoints = DirectionPoints.downPositive
val Direction.point get() = directionPoints[this]

class Day24(text: String, val debug: Debug = Debug.Disabled, private val maxRounds: Int = 20_000) {

    val start: Point
    val end: Point
    private val valleyBounds: Rectangle
    private val blizzardsByStep: MutableMap<Int, List<Blizzard>>
    private val blizzardPositionsByStep: MutableMap<Int, Set<Point>>

    init {
        val map = text.lines().map { it.toList() }
        start = Point(map.first().indexOf('.'), 0)
        end = Point(map.last().indexOf('.'), map.lastIndex)
        valleyBounds = Rectangle(1 until map.first().lastIndex, 1 until map.lastIndex)

        val blizzards = map.flatMapIndexed { row: Int, chars ->
            chars.mapIndexedNotNull { col, char ->
                val point = Point(col, row)
                when (char) {
                    '^' -> Blizzard(point, Direction.UP)
                    'v' -> Blizzard(point, Direction.DOWN)
                    '<' -> Blizzard(point, Direction.LEFT)
                    '>' -> Blizzard(point, Direction.RIGHT)
                    else -> null
                }
            }
        }

        blizzardsByStep = mutableMapOf(
            0 to blizzards
        )
        blizzardPositionsByStep = mutableMapOf(
            0 to blizzards.positions()
        )
    }

    fun solveA(): Int {
        return findShortestPath().getScore()
    }

    fun solveB(): Int {
        val toEnd = findShortestPath().end()
        val backToStart = findShortestPath(end, toEnd.iteration, start).end()
        val backToEnd = findShortestPath(start, backToStart.iteration, end).end()
        return backToEnd.iteration
    }

    private fun findShortestPath(start: Point = this.start, startingIteration: Int = 0, end: Point = this.end): GraphSearchResult<IterationData> {
        return findShortestPathByPredicate(
            IterationData(start, startingIteration),
            { it.position == end },
            { iterationData ->
                val iteration = iterationData.iteration
                val currentRoundBlizzards = blizzardsByStep[iteration]!!
                val nextIteration = nextIteration(iteration)
                val nextRoundBlizzards = blizzardsByStep.getOrPut(nextIteration) {
                    currentRoundBlizzards.map { it.next(valleyBounds) }
                }
                blizzardPositionsByStep.computeIfAbsent(nextIteration) { nextRoundBlizzards.positions() }
                val possibleNeighbours = iterationData.neighbours(nextIteration)
                val neighbours = possibleNeighbours.filter { (position, round) ->
                    (position in valleyBounds || position == end || position == start) && position !in blizzardPositionsByStep[round]!!
                            && round < maxRounds
                }

                debug {
                    println("====== Iteration $iteration ===== ")
                    printBoard(iteration, iterationData.position)
                    println("Current Position ${iterationData.position}")
                    println("Possible Neighbours $possibleNeighbours")
                    println("Only considering $neighbours")
                    println()
                }
                neighbours
            },
            heuristic = { (end - it.position).abs() }
        )
    }


    private fun printBoard(round: Int, position: Point) {
        val currentRoundBlizzards = blizzardsByStep[round]!!
        val rows = valleyBounds.yRange.count() + 2
        val columns = valleyBounds.xRange.count() + 2
        val board = Array(rows) {
            val array = Array(columns) { '.' }
            array[0] = '#'
            array[array.lastIndex] = '#'
            array
        }

        board[0] = Array(columns) { '#' }
        board[board.lastIndex] = Array(columns) { '#' }
        board[start] = '.'
        board[end] = '.'
        board[position] = 'E'

        currentRoundBlizzards.forEach {
            board[it.position] = it.direction.char
        }

        println(board.joinToString(separator = "\n") { it.joinToString(separator = "") })
    }

    private fun List<Blizzard>.positions() = mapTo(mutableSetOf()) { it.position }
    private fun nextIteration(iteration: Int) = iteration + 1

    data class IterationData(val position: Point, val iteration: Int) {
        fun neighbours(nextIteration: Int): Set<IterationData> {
            val pointNeighbours = position.neighbours()
            return buildSet {
                add(IterationData(position, nextIteration))
                pointNeighbours.forEach {
                    add(IterationData(it, nextIteration))
                }
            }
        }
    }


    data class Blizzard(val position: Point, val direction: Direction) {

        fun next(bounds: Rectangle): Blizzard {
            val next = position + direction.point
            return if (next in bounds) {
                copy(position = next)
            } else {
                when (direction) {
                    Direction.UP -> copy(position = Point(position.x, bounds.yRange.last))
                    Direction.DOWN -> copy(position = Point(position.x, bounds.yRange.first))
                    Direction.LEFT -> copy(position = Point(bounds.xRange.last, position.y))
                    Direction.RIGHT -> copy(position = Point(bounds.xRange.first, position.y))
                }
            }
        }
    }
}

//What is the fewest number of minutes required to avoid the blizzards and reach the goal?
fun solveA(text: String, debug: Debug = Debug.Disabled, maxRounds: Int = 20_000): Int {
    return Day24(text, debug, maxRounds).solveA()
}


fun solveB(text: String, debug: Debug = Debug.Disabled, maxRounds: Int = 20_000): Int {
    return Day24(text, debug, maxRounds).solveB()
}
