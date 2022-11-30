package helper

import java.util.concurrent.BlockingQueue
import java.util.stream.Stream
import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow

fun Int.toBinaryDigits(bitLength: Int): List<Int> = (bitLength - 1 downTo 0).map { bit ->
    val power = 2.pow(bit)
    val nextPower = power * 2

    (this@toBinaryDigits % nextPower) / power
}

fun Int.pow(n: Int) = this.toDouble().pow(n).toInt()

fun IntRange.size(): Int = max(0, last - first + 1)
fun Iterable<Long>.product() = reduce { acc, item -> acc * item }
fun Iterable<Int>.product() = reduce { acc, item -> acc * item }
fun Iterable<Int>.digitsToInt(radix: Int) = reduce { acc, digit -> acc * radix + digit }
fun Iterable<Int>.digitsToLong(radix: Int): Long = fold(0L) { acc, digit -> acc * radix + digit }
fun <E> MutableList<E>.removeFirstN(count: Int): List<E> = (0 until count).map { removeFirst() }

fun <A, B> Iterable<A>.cartesianProduct(other: Iterable<B>): List<Pair<A, B>> = flatMap { a -> other.map { b -> a to b } }
fun <A, B, R> Iterable<A>.cartesianProduct(other: Iterable<B>, transform: (A, B) -> R): List<R> = flatMap { a -> other.map { b -> transform(a, b) } }

fun <A, B> Sequence<A>.cartesianProduct(other: Sequence<B>): Sequence<Pair<A, B>> = flatMap { a -> other.map { b -> a to b } }
fun <A, B, R> Sequence<A>.cartesianProduct(other: Sequence<B>, transform: (A, B) -> R): Sequence<R> = flatMap { a -> other.map { b -> transform(a, b) } }
fun <A, B, R> Stream<A>.cartesianProduct(other: Iterable<B>, transform: (A, B) -> R): Stream<R> = flatMap { a -> other.map { b -> transform(a, b) }.stream() }

fun <T> ArrayList<T>.resize(minimumSize: Int, supplier: () -> T) {
    if (minimumSize < 0) {
        throw IllegalArgumentException("Negative sizes not allowed")
    }
    ensureCapacity(minimumSize)
    while (size < minimumSize) {
        add(supplier())
    }
}

fun <T> BlockingQueue<T>.drainToList(): List<T> {
    val outputList = mutableListOf<T>()
    drainTo(outputList)
    return outputList
}

val IntArray.abs: Int get() = sumOf { it.absoluteValue }
fun IntArray.mapToInt(transform: (Int) -> Int): IntArray = IntArray(this.size) { transform(this[it]) }
fun IntArray.mapIndexedToInt(transform: (index: Int, value: Int) -> Int): IntArray =
    IntArray(this.size) { transform(it, this[it]) }

fun <T : Comparable<T>> Iterable<T>.minAndMax(): Pair<T, T> {
    val iterator = iterator()
    if (!iterator.hasNext()) throw IllegalArgumentException("Cannot get min and max of empty collection")

    var min = iterator.next()
    var max = min
    while (iterator.hasNext()) {
        val next = iterator.next()
        when {
            next < min -> min = next
            next > max -> max = next
        }
    }
    return min to max
}

fun <K, V> Map<K, V>.pivot() = entries.associate { (key, value) -> value to key }

/**
 * Use the euclidean algorithm to find GCD(A,B)
 * https://www.khanacademy.org/computing/computer-science/cryptography/modarithmetic/a/the-euclidean-algorithm
 *  - GCD(A,0) = A
 *  - GCD(0,B) = B
 *  - If A = BQ + R and B!=0 then GCD(A,B) = GCD(B,R) where Q is an integer, R is an integer between 0 and B-1
 */
//inline fun gcd = ::greatestCommonDivisor
tailrec fun greatestCommonDivisor(a: Long, b: Long): Long {
    return when {
        a == 0L -> b
        b == 0L -> a
        else -> {
            val first = maxOf(a, b)
            val second = minOf(a, b)
            greatestCommonDivisor(second, first % second)
        }
    }
}

fun lowestCommonMultiple(a: Long, b: Long): Long = (a * b) / greatestCommonDivisor(a, b)
