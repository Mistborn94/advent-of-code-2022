package day12

import helper.graph.findShortestPath
import helper.graph.findShortestPathByPredicate
import helper.point.Point
import helper.point.contains
import helper.point.get
import helper.point.indexOf

typealias Predicate<K> = (K) -> Boolean

fun solveA(text: String): Int {
    val grid = text.lines().map { it.toList() }
    val end: Point = grid.indexOf('S')
    val start: Point = grid.indexOf('E')

    val result = findShortestPath(start, end, { point -> getNeighboursReversed(grid, point) })
    return result.getScore()
}

fun getNeighboursReversed(grid: List<List<Char>>, point: Point): List<Point> {
    val currentElevation = elevation(grid[point])
    return point.neighbours().filter {
        it in grid && (currentElevation - elevation(grid[it])) <= 1
    }
}

fun elevation(char: Char): Int = when (char) {
    'S' -> 0
    'E' -> 'z'.code - 'a'.code
    else -> char.code - 'a'.code
}

fun solveB(text: String): Int {
    val grid = text.lines().map { it.toList() }
    val endTest: Predicate<Point> = { grid[it] == 'S' || grid[it] == 'a' }
    val start: Point = grid.indexOf('E')

    val result = findShortestPathByPredicate(start, endTest, { point -> getNeighboursReversed(grid, point) })
    return result.getScore()
}
