package helper.point

import helper.abs
import kotlin.math.abs

class HyperspacePoint constructor(val parts: IntArray) {
    val size = parts.size

    val x get() = parts[0]
    val y get() = parts[1]
    val z get() = parts[2]

    val neighbours: List<HyperspacePoint>
        get() = neighbourOffsets(size - 1)
            .filter { it.abs != 0 }
            .map { this + it }
            .toList()

    operator fun get(i: Int) = parts[i]
    fun abs() = parts.sumOf { abs(it) }

    operator fun plus(other: HyperspacePoint) =
        HyperspacePoint(IntArray(minOf(size, other.size)) { parts[it] + other.parts[it] })

    operator fun plus(other: IntArray) = HyperspacePoint(IntArray(minOf(size, other.size)) { parts[it] + other[it] })

    operator fun minus(other: HyperspacePoint) =
        HyperspacePoint(IntArray(minOf(size, other.size)) { parts[it] - other.parts[it] })

    private fun neighbourOffsets(dimension: Int): List<IntArray> {
        return if (dimension == 0) {
            listOf(IntArray(1) { -1 }, IntArray(1) { 0 }, IntArray(1) { 1 })
        } else {
            neighbourOffsets(dimension - 1).flatMap { sequenceOf(it + -1, it + 0, it + 1) }
        }
    }

    override fun equals(other: Any?) = other is HyperspacePoint && parts.contentEquals(other.parts)
    override fun hashCode(): Int = parts.contentHashCode()
    override fun toString(): String = parts.joinToString(prefix = "[", postfix = "]", separator = ",")

    companion object {
        fun of(vararg parts: Int) = HyperspacePoint(parts)
        fun zero(dimensions: Int) = HyperspacePoint(IntArray(dimensions) { 0 })

        fun minOfComponents(a: HyperspacePoint, b: HyperspacePoint) =
            HyperspacePoint(IntArray(a.size) { i -> minOf(a[i], b[i]) })

        fun maxOfComponents(a: HyperspacePoint, b: HyperspacePoint) =
            HyperspacePoint(IntArray(a.size) { i -> maxOf(a[i], b[i]) })
    }
}