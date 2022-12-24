package helper.point

data class Rectangle(val xRange: IntRange, val yRange: IntRange) {
    operator fun contains(point: Point) = point.x in xRange && point.y in yRange
}