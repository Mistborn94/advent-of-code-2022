package day20

import helper.collections.GenericCircularList

const val decryptionKey = 811_589_153

fun solveA(text: String): Long {
    val inputs = text.lines()
        .map { it.toLong() }

    return findCoordinates(inputs, 1)
}

fun solveB(text: String): Long {
    val inputs = text.lines()
        .map { it.toLong() * decryptionKey }

    return findCoordinates(inputs, 10)
}

private fun findCoordinates(inputs: List<Long>, repCount: Int): Long {
    val nodes = inputs.map { GenericCircularList.Node(it) }
    nodes.first().prev = nodes.last()
    nodes.last().next = nodes.first()
    nodes.windowed(2).forEach { (a, b) ->
        a.next = b
        b.prev = a
    }
    val circularList = GenericCircularList(nodes.first())
    val zeroNode = nodes.first { it.value == 0L }

    repeat(repCount) {
        nodes.forEach { node ->
            circularList.moveNode(node, node.value)
        }
    }

    circularList.resetHead(zeroNode)
    circularList.shiftRight(1000)
    val first = circularList.head.value
    circularList.shiftRight(1000)
    val second = circularList.head.value
    circularList.shiftRight(1000)
    val third = circularList.head.value

    return first + second + third
}
