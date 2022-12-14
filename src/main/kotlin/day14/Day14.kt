package day14

import helper.point.Point
import helper.point.contains
import helper.point.get
import helper.point.set
import kotlin.math.max
import kotlin.math.min

//How many units of sand come to rest before sand starts flowing into the abyss below?

val entryPoint = Point(500, 0)

//1 down
//diagonal down and left (x -1)
//diagonal down and right (x + 1)
fun solveA(text: String): Int {
    val rockPaths = parseInput(text)
    val maxY = rockPaths.flatten().maxOf { it.y }
    val maxX = rockPaths.flatten().maxOf { it.x }
    val minX = rockPaths.flatten().minOf { it.x }
    val startingPoint = Point(entryPoint.x - minX, entryPoint.y)

    val grid = initGrid(minX, maxX, maxY, rockPaths, startingPoint)

    var sandUnits = 0
    var sandPos = restingPoint(grid, startingPoint)
    while (sandPos in grid) {
        grid[sandPos] = 'O'
        sandUnits += 1
        sandPos = restingPoint(grid, startingPoint)
    }

    return sandUnits
}

private fun drawPath(path: List<Point>, grid: MutableList<MutableList<Char>>, xOffset: Int) {
    path.windowed(2).forEach { (start, end) ->
        if (start.x == end.x) {
            val range = min(start.y, end.y)..max(start.y, end.y)
            range.forEach { y ->
                grid[Point(start.x - xOffset, y)] = '#'
            }
        } else {
            val range = min(start.x, end.x)..max(start.x, end.x)
            range.forEach { x ->
                grid[Point(x - xOffset, start.y)] = '#'
            }
        }
    }
}

fun restingPoint(grid: MutableList<MutableList<Char>>, startingPoint: Point): Point {
    var position = startingPoint
    while (position in grid) {
        val down = position + Point(0, 1)
        val ldown = position + Point(-1, 1)
        val rdown = position + Point(1, 1)

        position = when {
            down !in grid -> down
            grid[down] == '.' -> down
            ldown !in grid -> ldown
            grid[ldown] == '.' -> ldown
            rdown !in grid -> rdown
            grid[rdown] == '.' -> rdown
            else -> break
        }
    }
    return position
}

private fun printGrid(grid: MutableList<MutableList<Char>>) {
    println("Grid is: ")
    println(grid.joinToString(separator = "\n") { it.joinToString(separator = "") })
}

fun solveB(text: String): Int {
    val rockPaths = parseInput(text)

    val maxY = rockPaths.flatten().maxOf { it.y } + 2
    val maxX = entryPoint.x + maxY
    val minX = entryPoint.x - maxY
    val startingPoint = Point(entryPoint.x - minX, entryPoint.y)

    val grid = initGrid(minX, maxX, maxY, rockPaths, startingPoint)
    drawPath(listOf(Point(minX, maxY), Point(maxX, maxY)), grid, minX)
    var sandUnits = 0
    var sandPos = restingPoint(grid, startingPoint)
    while (grid[startingPoint] == '+') {
        grid[sandPos] = 'O'
        sandUnits += 1
        sandPos = restingPoint(grid, startingPoint)

        if (sandPos !in grid) {
            throw IllegalStateException("$sandPos is outside grid (x $minX to $maxX)")
        }
    }

//    printGrid(grid)

    return sandUnits
}

private fun initGrid(
    minX: Int,
    maxX: Int,
    maxY: Int,
    rockPaths: List<List<Point>>,
    startingPoint: Point
): MutableList<MutableList<Char>> {
    val grid = newGrid(maxY, maxX, minX)
    rockPaths.forEach { path ->
        drawPath(path, grid, minX)
    }
    grid[startingPoint] = '+'
    return grid
}

private fun parseInput(text: String) = text.lines().map { lines ->
    lines.split(" -> ").map { point ->
        val (x, y) = point.split(",")
        Point(x.toInt(), y.toInt())
    }
}

private fun newGrid(maxY: Int, maxX: Int, xOffset: Int) = Array(maxY + 1) { Array(maxX - xOffset + 1) { '.' }.toMutableList() }.toMutableList()
