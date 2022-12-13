package day13

import java.util.*
import kotlin.math.min

fun solveA(text: String): Int {
    val packets = text.lines().chunked(3).map { parseLine(it.first()) to parseLine(it[1]) }

    return packets.mapIndexed { index, (a, b) ->
        if (a < b) {
            index + 1
        } else {
            0
        }
    }.sum()
}

fun solveB(text: String): Int {
    val packets = (text.lines() + listOf("[[2]]", "[[6]]"))
        .filter { it.isNotBlank() }
        .map { parseLine(it) }
        .sorted()

    val a = packets.indexOfFirst { it.toString() == "[[2]]" } + 1
    val b = packets.indexOfFirst { it.toString() == "[[6]]" } + 1

    return a * b
}

fun parseLine(line: String): Packet {
    val stack = Stack<Packet.ListPacket>()
    val iterator = line.iterator()
    val rootPacket = Packet.ListPacket(mutableListOf())
    stack.push(rootPacket)
    iterator.next()
    var intString = ""
    while (iterator.hasNext()) {
        when (val char = iterator.nextChar()) {
            '[' -> {
                val newPacket = Packet.ListPacket(mutableListOf())
                stack.peek().data.add(newPacket)
                stack.push(newPacket)
            }

            ']' -> {
                if (intString.isNotEmpty()) {
                    stack.peek().data.add(Packet.IntPacket(intString.toInt()))
                    intString = ""
                }
                stack.pop()
            }

            ',' -> {
                if (intString.isNotEmpty()) {
                    stack.peek().data.add(Packet.IntPacket(intString.toInt()))
                    intString = ""
                }
            }

            in '0'..'9' -> {
                intString += char
            }
        }
    }

    if (rootPacket.toString() != line) {
        throw java.lang.IllegalStateException("Bad parsing! Parsed: $rootPacket vs original $line")
    }

    if (!stack.empty()) throw IllegalStateException("Messed up parsing")
    return rootPacket
}

sealed class Packet : Comparable<Packet> {

    abstract fun toListPackage(): ListPacket
    abstract override fun toString(): String

    class IntPacket(val data: Int) : Packet() {
        override fun toListPackage(): ListPacket {
            return ListPacket(mutableListOf(this))
        }

        override fun toString(): String = data.toString()

        override fun compareTo(other: Packet): Int {
            return when (other) {
                is IntPacket -> this.data.compareTo(other.data)
                else -> this.toListPackage().compareTo(other)
            }
        }
    }

    class ListPacket(val data: MutableList<Packet>) : Packet() {
        override fun toListPackage(): ListPacket = this
        override fun toString(): String {
            return data.joinToString(separator = ",", prefix = "[", postfix = "]")
        }

        override fun compareTo(other: Packet): Int {
            val other = other.toListPackage()
            val minLength = min(this.data.size, other.data.size)

            for (i in 0 until minLength) {
                val compare = this.data[i].compareTo(other.data[i])
                if (compare != 0) {
                    return compare
                }
            }
            return data.size.compareTo(other.data.size)
        }
    }

}