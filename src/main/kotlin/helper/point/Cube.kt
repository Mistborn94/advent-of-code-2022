package helper.point

data class Cube(val xRange: IntRange, val yRange: IntRange, val zRange: IntRange) {

    fun grow(size: Int = 1): Cube {
        return Cube(
            xRange.first - size..xRange.last + size,
            yRange.first - size..yRange.last + size,
            zRange.first - size..zRange.last + size
        )
    }

    operator fun contains(point: Point3D) = point.x in xRange && point.y in yRange && point.z in zRange

    companion object {
        fun boundingBoxOf(points: MutableSet<Point3D>): Cube {
            var minX = Int.MAX_VALUE
            var maxX = Int.MIN_VALUE

            var minY = Int.MAX_VALUE
            var maxY = Int.MIN_VALUE

            var minZ = Int.MAX_VALUE
            var maxZ = Int.MIN_VALUE

            points.forEach {
                minX = minOf(minX, it.x)
                maxX = maxOf(maxX, it.x)

                minY = minOf(minY, it.y)
                maxY = maxOf(maxY, it.y)

                minZ = minOf(minZ, it.z)
                maxZ = maxOf(maxZ, it.z)
            }

            return Cube(minX..maxX, minY..maxY, minZ..maxZ)
        }
    }
}