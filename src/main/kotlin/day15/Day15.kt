package day15

import helper.CompositeRange
import helper.point.Point
import kotlin.math.absoluteValue

val pattern = """Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)""".toRegex()

//In the row where y=2000000, how many positions cannot contain a beacon?
fun solveA(text: String, targetY: Int = 2_000_000): Int {
    val sensors = parseInput(text)

    val noBeacon = CompositeRange(emptyList())

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
        val possibleRanges = CompositeRange(listOf(targetRange))
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

fun coveredRange(point: Point, radius: Int, targetY: Int): IntRange? {
    val yOff = (targetY - point.y).absoluteValue
    val rowRadius = radius - yOff
    return if (yOff > radius) {
        null
    } else {
        point.x - rowRadius..point.x + rowRadius
    }
}

