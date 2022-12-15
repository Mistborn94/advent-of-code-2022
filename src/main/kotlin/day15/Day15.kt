package day15

import helper.CompositeRange
import helper.cartesianProduct
import helper.overlap
import helper.point.Point
import kotlin.math.absoluteValue

val pattern = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

//In the row where y=2000000, how many positions cannot contain a beacon?
fun solveA(text: String, targetY: Int = 2_000_000): Int {
    val sensors = parseInput(text)
    val noBeacon = CompositeRange()

    sensors.forEach { (sensor, beacon) ->
        val radius = sensor.minus(beacon).abs()
        val coveredRange = coveredRange(sensor, radius, targetY)
        if (coveredRange != null) {
            noBeacon.add(coveredRange)
        }
        if (beacon.y == targetY) {
            noBeacon.subtract(beacon.x..beacon.x)
        }
    }

    return noBeacon.count()
}

private fun parseInput(text: String): List<Pair<Point, Point>> {
    val sensors = text.lines().map { line ->
        val (_, xa, ya, xb, yb) = pattern.matchEntire(line)!!.groupValues
        val sensor = Point(xa.toInt(), ya.toInt())
        val beacon = Point(xb.toInt(), yb.toInt())
        sensor to beacon
    }
    return sensors
}

//distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.
fun solveB(text: String, targetMax: Int = 4_000_000): Long {
    val sensors = parseInput(text)

    var foundX: Int? = null
    var foundY: Int? = null
    val targetRange = 0..targetMax
    for (targetY in targetRange) {
        val possibleRanges = CompositeRange(targetRange)
        for ((sensor, beacon) in sensors) {
            val radius = sensor.minus(beacon).abs()
            val coveredRange = coveredRange(sensor, radius, targetY)
            if (coveredRange != null) {
                possibleRanges.subtract(coveredRange)
            }
        }
        val count = possibleRanges.count()
        if (count > 1) {
            throw IllegalStateException("Shouldn't be possible")
        } else if (count == 1) {
            foundY = targetY
            foundX = possibleRanges.first()
            break
        }
    }

    if (foundX == null || foundY == null) {
        throw IllegalStateException("Nothing found")
    }

    return foundX * 4_000_000L + foundY
}

fun solveB2(text: String, targetMax: Int = 4_000_000): Long {
    val diamonds = parseInput(text).map { (sensor, beacon) ->
        val radius = (beacon - sensor).abs() + 1
        Diamond(sensor, radius)
    }
    val targetRange = 0..targetMax

    return diamonds.asSequence().cartesianProduct(diamonds.asSequence())
        .filter { (a, b) -> a != b }
        .flatMap { (a, b) -> a.intersect(b).asSequence() }
        .distinct()
        .first { point -> point.x in targetRange && point.y in targetRange && diamonds.none { it.surrounds(point) } }
        .let { (x, y) -> x * 4_000_000L + y }
}

fun solveB3(text: String, targetMax: Int = 4_000_000): Long {
    val diamonds = parseInput(text).map { (sensor, beacon) ->
        val radius = (beacon - sensor).abs()
        Diamond(sensor, radius)
    }
    val targetRange = 0..targetMax

    val boundaries = diamonds.asSequence()
        .flatMap { it.boundaries(targetRange) }
        .toList()

    return boundaries
        .mapNotNull { (line, range) ->
            diamonds.forEach {
                range.subtract(it.overlapRange(line))
            }
            if (range.isEmpty()) {
                null
            } else {
                val x = range.first()
                val y = line.y(x)
                Point(x, y)
            }
        }
        .first { (x, y) -> x in targetRange && y in targetRange }
        .let { (x, y) -> x * 4_000_000L + y }
}

data class Diamond(val center: Point, val radius: Int) {
    private val xRange = center.x - radius..center.x + radius

    private val a: Int get() = center.x
    private val b: Int get() = center.y;
    private val r: Int get() = radius + 1

    private val topLeft: Line by lazy { Line(-1, a - r + b) }
    private val bottomRight: Line by lazy { Line(-1, a + r + b) }

    private val topRight: Line by lazy { Line(1, -a - r + b) }
    private val bottomLeft: Line by lazy { Line(1, -a + r + b) }

    fun intersect(other: Diamond): List<Point> {
        return listOf(
            this.topLeft to other.topRight,
            this.bottomRight to other.topRight,
            this.topLeft to other.bottomLeft,
            this.bottomRight to other.bottomLeft
        ).flatMap { (a, b) ->
            val x = (a.c - b.c) / 2
            listOf(
                Point(x, a.y(x)), Point(x, b.y(x))
            )
        }
    }

    fun boundaries(targetRange: IntRange): List<Pair<Line, CompositeRange>> {
        val leftRange = (targetRange.first..center.x).overlap(xRange)
        val rightRange = (center.x..targetRange.last).overlap(xRange)
        return listOf(
            topLeft to CompositeRange(leftRange),
            bottomLeft to CompositeRange(leftRange),
            topRight to CompositeRange(rightRange),
            bottomRight to CompositeRange(rightRange),
        )
    }

    fun overlapRange(line: Line): IntRange {
        return if (line.m == -1) {
            //parallel with top left/ bottom right
            if (outOfBounds(topLeft.c, bottomRight.c, line.c)) {
                IntRange.EMPTY
            } else {
                overlayRange(line.c - topLeft.c)
            }
        } else {
            //parallel with top right/ bottom left
            if (outOfBounds(topRight.c, bottomLeft.c, line.c)) {
                IntRange.EMPTY
            } else {
                overlayRange(bottomLeft.c - line.c)
            }
        }
    }

    private fun overlayRange(offset: Int): IntRange {
        val expectedLength = radius + offset % 2
        val firstX = xRange.first + offset / 2
        return firstX until firstX + expectedLength
    }

    private fun outOfBounds(top: Int, bottom: Int, line: Int) = line >= bottom || line <= top

    fun surrounds(point: Point): Boolean {
        val x = point.x
        val y = point.y
        return (x > xRange.first && x < xRange.last) && if (x < center.x) {
            y > topLeft.y(x) && y < bottomLeft.y(x)
        } else {
            y > topRight.y(x) && y < bottomRight.y(x)
        }
    }

}

data class Line(val m: Int, val c: Int) {
    fun y(x: Int): Int = m * x + c
}

fun coveredRange(point: Point, radius: Int, targetY: Int): IntRange? {
    val yOff = (targetY - point.y).absoluteValue
    val rowRadius = radius - yOff
    return if (yOff > radius) {
        null
    } else {
        point.x - rowRadius..point.x + rowRadius
    }
}

