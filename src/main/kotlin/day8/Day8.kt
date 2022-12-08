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
//        println("Tree $point is on the edge, score is 0")
        return 0
    }

//    println("Starting Tree <$col,$row> [$treeHeight]")
    var a = 0
    for (r in (row - 1).downTo(0)) {
//        println("Checking a <$r,$col> [${trees[r][col}]")
        a += 1
        if (trees[r][col] >= treeHeight) {
            break;
        }
    }

    var b = 0
    for (r in (row + 1 until height)) {
        b += 1
        if (trees[r][col] >= treeHeight) {
            break;
        }
    }

    var sc = 0
    for (c in (col - 1).downTo(0)) {
        sc += 1
        if (trees[row][c] >= treeHeight) {
            break;
        }
    }

    var d = 0
    for (c in col + 1 until width) {
        d += 1
        if (trees[row][c] >= treeHeight) {
            break;
        }
    }
    //left

//    println("Tree <$row,$col> [$treeHeight] distances are [$a, $b, $sc, $d] score is ${a * b * sc * d}")
    return a * b * sc * d
}
