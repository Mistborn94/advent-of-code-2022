package day9

import helper.point.Point
import kotlin.math.abs

fun solveA(text: String): Int {

    val start = Point(0, 0)

    return getMoveSequence(text)
        .scan(start to start) { (head, tail), headMove ->
            val nextHead = head + headMove
            val nextTail = calcNext(nextHead, tail)
            nextHead to nextTail
        }
        .distinctBy { it.second }
        .count()
}

private fun getMoveSequence(text: String) = text.lineSequence().flatMap {
    val (dir, count) = it.split(" ")
    val move = when (dir) {
        "R" -> Point(1, 0)
        "L" -> Point(-1, 0)
        "U" -> Point(0, -1)
        "D" -> Point(0, 1)
        else -> throw IllegalArgumentException(dir)
    }
    generateSequence { move }.take(count.toInt())
}

private fun calcNext(currentHead: Point, currentTail: Point): Point {
    val diff = currentHead - currentTail

    val xMovement = calcMovement(diff.x)
    val yMovement = calcMovement(diff.y)

    return if (abs(diff.x) > 1 || abs(diff.y) > 1) {
        currentTail + Point(xMovement, yMovement)
    } else {
        currentTail
    }
}

private fun calcMovement(coord: Int) = if (coord == 0) 0 else if (coord < 0) -1 else 1

fun solveB(text: String): Int {
    val initialRope = (0 until 10).map { Point(0, 0) }
    return getMoveSequence(text)
        .scan(initialRope) { rope, headMove ->
            rope.scanIndexed(emptyList()) { i: Int, acc: List<Point>, point: Point ->
                if (i == 0) {
                    listOf(point + headMove)
                } else {
                    acc + calcNext(acc.last(), point)
                }
            }.last()
        }
        .distinctBy { it.last() }
        .count()
}

