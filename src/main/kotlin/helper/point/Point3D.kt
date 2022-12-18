package helper.point

import kotlin.math.absoluteValue

data class Point3D(val x: Int, val y: Int, val z: Int) {

    operator fun plus(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }

    operator fun minus(other: Point3D): Point3D {
        return Point3D(x - other.x, y - other.y, z - other.z)
    }

    fun neighbours(): List<Point3D> = listOf(
        this - x1,
        this + x1,
        this - y1,
        this + y1,
        this - z1,
        this + z1
    )

    fun abs(): Int = x.absoluteValue + y.absoluteValue + z.absoluteValue

    companion object {
        private val x1 = Point3D(1, 0, 0)
        private val y1 = Point3D(0, 1, 0)
        private val z1 = Point3D(0, 0, 1)
    }
}