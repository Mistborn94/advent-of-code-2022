package day9

import helper.point.Point
import kotlin.math.abs

fun solveA(text: String): Int {

    var currentHead = Point(0, 0)
    var currentTail = Point(0, 0)
    val tailPosSeen = mutableSetOf<Point>(currentTail)

    text.lines().forEach { line ->
        val (count, headMove) = parseLine(line)
        repeat(count) {
            currentHead += headMove
            val diff = currentHead - currentTail

//            tailPosSeen.add(calcNext(currentHead, currentTail))
            if (abs(diff.x) > 1 || abs(diff.y) > 1) {
//                println("Head $currentHead, Tail $currentTail, Diff $diff")
                //tail must move
                val xMovement = calMovement(diff.x)
                val yMovement = calMovement(diff.y)
                currentTail = calcNext(currentHead, currentTail)
                tailPosSeen.add(currentTail)
            }
//            println("After {$line}-$it head is $currentHead and tail is $currentTail")

        }
    }
    return tailPosSeen.size
}

private fun calcNext(currentHead: Point, currentTail: Point): Point {
    val diff = currentHead - currentTail

    val xMovement = calMovement(diff.x)
    val yMovement = calMovement(diff.y)

    return if (abs(diff.x) > 1 || abs(diff.y) > 1) {
        currentTail + Point(xMovement, yMovement)
    } else {
        currentTail
    }
}

private fun calMovement(coord: Int) = if (coord == 0) 0 else if (coord < 0) -1 else 1


fun solveB(text: String): Int {
    var rope = (0 until 10).map { Point(0, 0) }
    val tailPosSeen = mutableSetOf(Point(0, 0))

    text.lines().forEach { line ->
        val (count, headMove) = parseLine(line)
        repeat(count) {
            val newRope = rope.toMutableList()
            newRope[0] = rope.first() + headMove

            rope.indices.forEach { index ->
                if (index > 0) {
                    newRope[index] = calcNext(newRope[index - 1], rope[index])
                }
            }

            rope = newRope
            tailPosSeen.add(rope.last())

        }
    }
    return tailPosSeen.size
}

private fun parseLine(line: String): Pair<Int, Point> {
    val (dir, countS) = line.split(" ")
    val count = countS.toInt()

    val headMove = when (dir) {
        "R" -> Point(1, 0)
        "L" -> Point(-1, 0)
        "U" -> Point(0, -1)
        "D" -> Point(0, 1)
        else -> throw IllegalArgumentException(dir)
    }
    return Pair(count, headMove)
}
