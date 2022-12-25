package day25

import helper.Debug

//What SNAFU number do you supply to Bob's console?
fun solveA(text: String, debug: Debug = Debug.Disabled): String = toSnafu(text.lines().sumOf { parseSnafu(it) })

fun toSnafu(input: Long): String {
    var snafuNumber = ""

    var number = input

    while (number > 0L) {
        val (digit, remainder) = snafuDigit(number)
        snafuNumber = "$digit$snafuNumber"
        number = remainder
    }

    val parseSnafu = parseSnafu(snafuNumber)
    assert(parseSnafu == input) { "Snafu building for $input failed: $snafuNumber == $parseSnafu" }
    return snafuNumber.trimStart('0')
}

/**
 * Returns digit and overflow for a snafu digit
 */
private fun snafuDigit(number: Long): Pair<String, Long> {
    val digit = number % 5
    val remainder = number / 5
    return when (digit) {
        4L -> "-" to 1 + remainder
        3L -> "=" to 1 + remainder
        else -> digit.toString() to remainder
    }
}

fun parseSnafu(it: String): Long {
    val iterator = it.reversed().iterator()
    var number = 0L
    var position = 1L

    while (iterator.hasNext()) {
        val digit = when (val char = iterator.nextChar()) {
            '-' -> -1
            '=' -> -2
            else -> char.digitToInt()
        }
        number += digit * position
        position *= 5
    }

    return number
}
