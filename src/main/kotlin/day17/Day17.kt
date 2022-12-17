package day17

import helper.collections.GenericCircularList
import helper.collections.PeekingIterator
import helper.point.Point
import java.util.*

val allShapes = listOf(
    """####""",
    """ |.#.
        |###
        |.#.""".trimMargin().trim(),

    """ |..#
        |..#
        |###""".trimMargin().trim(),

    """ |#
        |#
        |#
        |# """.trimMargin().trim(),

    """ |##
        |##""".trimMargin().trim()
)
    .mapIndexed { i, string -> Shape(i, parsePoints(string)) }

private fun parsePoints(string: String) = string.lines().reversed().flatMapIndexed { y, s ->
    s.mapIndexedNotNull { x, c ->
        if (c == '#') {
            Point(x, y)
        } else {
            null
        }
    }
}

private const val LEFT_WALL = -1
private const val RIGHT_WALL = 7
private const val LEFT_START = LEFT_WALL + 3
private const val CYCLE_SIMULATION_LENGTH = 5000

class Shape(val type: Int, val points: List<Point>) {

    fun bottom() = points.minOf { it.y }
    fun left() = points.minOf { it.x }
    fun right() = points.maxOf { it.x }

    fun translateForStart(targetBottom: Int, targetLeft: Int): Shape {
        val yOffset = targetBottom - bottom()
        val xOffset = targetLeft - left()
        return Shape(type, points.map { (x, y) -> Point(x + xOffset, y + yOffset) })
    }

    fun pushLeft(): Shape {
        return if (left() > LEFT_WALL + 1) {
            Shape(type, points.map { (x, y) -> Point(x - 1, y) })
        } else {
            this
        }
    }

    fun pushRight(): Shape {
        return if (right() < RIGHT_WALL - 1) {
            Shape(type, points.map { (x, y) -> Point(x + 1, y) })
        } else {
            this
        }
    }

    fun moveDown(): Shape {
        return Shape(type, points.map { (x, y) -> Point(x, y - 1) })
    }

    fun overlaps(points: Set<Point>) = this.points.any { it in points }
    fun overlaps(grid: Grid) = this.points.any { it in grid }
}

//For my grid : higher y is up, lower y is down
fun solveA(input: String, count: Int = 2022, log: Int = 0): List<Int> {
    val possibleShapes = GenericCircularList.ofValues(allShapes).iterator()
    val inputWithIndex = input.trim().toList().withIndex().toList()
    val jets = GenericCircularList.ofValues(inputWithIndex).iterator()

    val grid = Grid()

    repeat(count) {
        val startingIndex = grid.maxY() + 4
        val startingShape = possibleShapes.next().translateForStart(startingIndex, LEFT_START)
        val shape = simulateFall(jets, startingShape, grid)
        grid.addAll(shape.points)
        if (it in count - log until count)
            println("Iteration ${it}: Max heights are ${grid.maxYs()}")
    }

    return grid.maxYs()
}

private fun pushAndCheckCollision(jet: Char, shape: Shape, grid: Grid): Shape {
    val pushed = if (jet == '<') shape.pushLeft() else shape.pushRight()
    return if (pushed.overlaps(grid)) {
        shape
    } else {
        pushed
    }
}

data class IterationDetails(val iteration: Int, val shapeType: Int, val jetId: Int, val heightDelta: List<Int>)

fun solveB(input: String, targetIteration: Long = 1_000_000_000_000L): List<Long> {
    val possibleShapes = GenericCircularList.ofValues(allShapes).iterator()
    val jets = GenericCircularList.ofValues(input.trim().withIndex()).iterator()

    val iterationHeightMaps = mutableListOf<List<Int>>()

    val loopStartIteration: Int
    val loopEndIteration: Int
    val simulation = ArrayList<IterationDetails>(CYCLE_SIMULATION_LENGTH)
    val cyclesMap = mutableMapOf<Pair<Int, Int>, MutableList<IterationDetails>>()

    val grid = Grid()
    var iteration = 0
    while (true) {
        val startingShape = possibleShapes.next().translateForStart(grid.maxY() + 4, LEFT_START)
        val shapeType = startingShape.type

        val startingJetId = jets.peek().index
        val startingHeights = grid.maxYs()

        val endingShape = simulateFall(jets, startingShape, grid)
        grid.addAll(endingShape.points)
        val endingHeights = grid.maxYs()
        iterationHeightMaps.add(endingHeights)

        val heightDelta = endingHeights.subtractItems(startingHeights)
        val similarIterations = cyclesMap.getOrPut(shapeType to startingJetId) { mutableListOf() }
        val identicalDeltas = similarIterations.filter { it.heightDelta == heightDelta }

        if (identicalDeltas.size > 1) {
            val middleCandidate = identicalDeltas.last().iteration
            val firstCandidate = identicalDeltas[identicalDeltas.lastIndex - 1].iteration

            val loopSizeSecond = iteration - middleCandidate
            val loopSizeFirst = middleCandidate - firstCandidate
            val totalDeltaSecond = iterationHeightMaps.iterationHeightDelta(middleCandidate, iteration)
            val totalDeltaFirst = iterationHeightMaps.iterationHeightDelta(firstCandidate, middleCandidate)

            if (loopSizeSecond == loopSizeFirst && totalDeltaSecond == totalDeltaFirst) {
                loopStartIteration = identicalDeltas.first().iteration
                loopEndIteration = identicalDeltas[1].iteration
                break
            }
        }

        val iterationDetails = IterationDetails(iteration, shapeType, startingJetId, heightDelta)
        similarIterations.add(iterationDetails)
        simulation.add(iterationDetails)
        iteration++
    }

    val loopSize = loopEndIteration - loopStartIteration
    val remainingIterations = targetIteration - loopEndIteration
    val loopCount = remainingIterations / loopSize
    val loopRemainder = remainingIterations % loopSize - 1

    val loopEndHeights = iterationHeightMaps[loopEndIteration]
    val loopDelta = iterationHeightMaps.iterationHeightDelta(loopStartIteration, loopEndIteration)
    val remainderDelta = iterationHeightMaps.iterationHeightDelta(loopStartIteration, loopStartIteration + loopRemainder.toInt())

    solveA(input, loopStartIteration + 1, 3)
    solveA(input, loopEndIteration + 1, 3)

    val finalHeights = loopEndHeights.mapIndexed { index, item ->
        item.toLong() + loopDelta[index] * loopCount + remainderDelta[index]
    }

    return finalHeights
}

private fun MutableList<List<Int>>.iterationHeightDelta(start: Int, end: Int) = this[end].subtractItems(this[start])
private fun List<Int>.subtractItems(other: List<Int>) = mapIndexed { i, item -> item - other[i] }

private fun simulateFall(jets: PeekingIterator<IndexedValue<Char>>, startingShape: Shape, grid: Grid): Shape {
    var rest = false
    var shape = startingShape
    while (!rest) {
        val (_, jet) = jets.next()
        val pushed = pushAndCheckCollision(jet, shape, grid)
        val newShape = pushed.moveDown()

        if (newShape.overlaps(grid)) {
            rest = true
            shape = pushed
        } else {
            shape = newShape
        }

    }
    return shape
}

private val columnRange = LEFT_WALL + 1 until RIGHT_WALL
private val comparator: Comparator<Int> = Comparator.naturalOrder<Int>().reversed()

class Grid {

    private val columns = columnRange.map { TreeSet(comparator) }

    init {
        columnRange.forEach {
            columns[it].add(0)
        }
    }

    fun maxY(x: Int) = columns[x].first()
    fun maxY() = columnRange.maxOf { maxY(it) }
    fun maxYs() = columnRange.map { maxY(it) }

    fun add(point: Point) {
        columns[point.x].add(point.y)
    }

    fun addAll(points: Iterable<Point>) {
        points.forEach { add(it) }
    }

    fun contains(x: Int, y: Int) = columns[x].contains(y)
    operator fun contains(point: Point) = columns[point.x].contains(point.y)

    override fun toString(): String {
        return (maxY() downTo 0).joinToString(separator = "\n") { y ->
            columnRange.joinToString(separator = "", prefix = "|", postfix = "|") { x ->
                if (this.contains(x, y)) "#" else "."
            }
        }
    }

}