package day25

import helper.Debug

//What SNAFU number do you supply to Bob's console?
fun solveA(text: String, debug: Debug = Debug.Disabled): String {
    return toSnafu(text.lines().sumOf { parseSnafu(it) })
}

fun toSnafu(input: Long): String {
    val base5Str = input.toString(5)
    val digits = mutableListOf<Int>()

    val iterator = base5Str.map { it.digitToInt() }.reversed().iterator()
    var overflowDigit = 0

    while (iterator.hasNext()) {
        val next = iterator.next()
        val (digit, overflow) = snafuDigit(next + overflowDigit)
        digits.add(digit)
        overflowDigit = overflow
    }

    while (overflowDigit != 0) {
        val (digit, overflow) = snafuDigit(overflowDigit)
        digits.add(digit)
        overflowDigit = overflow
    }

    val snafuNumber = digits.reversed().joinToString("") {
        when (it) {
            -1 -> "-"
            -2 -> "="
            else -> "$it"
        }
    }

    val parseSnafu = parseSnafu(snafuNumber)
    assert(parseSnafu == input) { "Snafu building for $input failed: $snafuNumber == $parseSnafu" }
    return snafuNumber.trimStart('0')
}

/**
 * Returns digit and overflow for a snafu digit
 */
private fun snafuDigit(digit: Int): Pair<Int, Int> = when {
    digit >= 5 -> digit % 5 to digit / 5
    digit == 4 -> -1 to 1
    digit == 3 -> -2 to 1
    else -> digit to 0
}

fun parseSnafu(it: String): Long {
    val iterator = it.reversed().iterator()
    var num = 0L
    var n = 1L

    while (iterator.hasNext()) {
        val digit = when (val char = iterator.nextChar()) {
            '-' -> -1
            '=' -> -2
            else -> char.digitToInt()
        }
        num += digit * n
        n *= 5
    }

    return num
}
