package day8

import helper.point.Point
import helper.point.points

fun solveA(text: String): Int {
    val trees = text.lines().map { line -> line.map { it.digitToInt() } }

    val height = trees.size
    val width = trees[0].size
    val visible = trees.map { line -> line.mapTo(mutableListOf()) { false } }

    //from left /right
    for (row in 0 until height) {
        var highestL = -1
        var highestR = -1

        for (col in 0 until width) {
            if (trees[col][row] > highestL) {
                highestL = trees[col][row]
                visible[col][row] = true
            }

            val otherCol = width - col - 1
            if (trees[otherCol][row] > highestR) {
                highestR = trees[otherCol][row]
                visible[otherCol][row] = true
            }
        }
    }

    //from top/bottom
    for (col in 0 until width) {
        var highestT = -1
        var highestB = -1
        for (row in 0 until height) {

            if (trees[col][row] > highestT) {
                highestT = trees[col][row]
                visible[col][row] = true
            }

            val otherRow = height - row - 1
            if (trees[col][otherRow] > highestB) {
                highestB = trees[col][otherRow]
                visible[col][otherRow] = true
            }
        }
    }

    return visible.sumOf { row -> row.count { it } }
}


fun solveB(text: String): Int {
    val map = text.lines().map { line ->
        line.map { it.digitToInt() }
    }

    return map.points().maxOf { score(it, map) }
}

fun score(point: Point, trees: List<List<Int>>): Int {

    val (col, row) = point

    val height = trees.size
    val width = trees[0].size

    val treeHeight = trees[row][col]

    if (col == 0 || row == 0 || col == width - 1 || row == height - 1) {
        return 0
    }

    val up = calcVertical((row - 1).downTo(0), trees, col, treeHeight)
    val down = calcVertical(row + 1 until height, trees, col, treeHeight)
    val left = calcHorizontal((col - 1).downTo(0), trees, row, treeHeight)
    val right = calcHorizontal(col + 1 until width, trees, row, treeHeight)

    return up * down * left * right
}

private fun calcVertical(range: IntProgression, trees: List<List<Int>>, col: Int, treeHeight: Int): Int {
    val index = range.indexOfFirst { r -> trees[r][col] >= treeHeight }
    return if (index == -1) range.count() else index + 1
}

private fun calcHorizontal(range: IntProgression, trees: List<List<Int>>, row: Int, treeHeight: Int): Int {
    val index = range.indexOfFirst { c -> trees[row][c] >= treeHeight }
    return if (index == -1) range.count() else index + 1
}
