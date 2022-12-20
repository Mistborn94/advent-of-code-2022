package helper.collections

import kotlin.math.absoluteValue

/**
 * Circular list containing unique values
 */
class GenericCircularList<T>(var head: Node<T>) : Iterable<T> {

    private var listSize = forwardsSize()

    private fun forwardsSize(max: Int = Int.MAX_VALUE): Int {
        var size = 1
        var current = head
        while (current.next != head) {
            size += 1
            current = current.next
            if (size > max) {
                throw IllegalStateException("Forwards Size - List is getting too big :(")
            }
        }
        return size
    }

    fun remove(node: Node<T>): T {
        if (head == node) {
            head = node.next
        }
        node.remove()
        listSize -= 1
        return node.value
    }

    fun resetHead(newHead: Node<T>) {
        head = newHead
    }

    fun addAfter(startNode: Node<T>, elements: Collection<T>) {
        elements.iterator()
        val end = startNode.next
        var previous = startNode
        elements.forEach { value ->
            val newNode = Node(value)
            newNode.prev = previous
            previous.next = newNode
            previous = newNode
        }
        previous.next = end
        end.prev = previous
        listSize += elements.size
    }

    private fun moveTo(previous: Node<T>, movedNode: Node<T>) {
        if (previous != movedNode.prev) {
            val newNext = previous.next
            previous.next = movedNode
            movedNode.prev = previous
            movedNode.next = newNext
            newNext.prev = movedNode

            if (head == movedNode.next) {
                head = movedNode
            }
        }
    }

    override fun toString(): String {
        return buildString {
            append("[")
            append(head.value)
            var next = head.next
            while (next != head) {
                append(", ")
                append(next.value)
                next = next.next
            }
            append("]")
        }
    }


    fun shiftRight(count: Int = 1) {
        repeat(count) {
            head = head.next
        }
    }

    fun shiftLeft(count: Int = 1) {
        repeat(count) {
            head = head.prev
        }
    }

    override fun iterator(): PeekingIterator<T> {
        return object : PeekingIterator<T> {
            var currentNode = head
            override fun hasNext(): Boolean = true
            override fun next(): T {
                val value = currentNode.value
                currentNode = currentNode.next
                return value
            }

            override fun peek(): T = currentNode.value

        }
    }


    fun moveNode(node: Node<T>, steps: Long) {
        if (steps < 0) {
            var newPrev = node.prev
            node.remove()
            val times = steps.absoluteValue % (listSize - 1)
            repeat(times.toInt()) {
                newPrev = newPrev.prev
            }

            moveTo(newPrev, node)
        } else if (steps > 0) {
            var newPrev = node.next
            node.remove()
            val times = (steps - 1) % (listSize - 1)
            repeat(times.toInt()) {
                newPrev = newPrev.next
            }

            moveTo(newPrev, node)
        }
    }

    class Node<T>(val value: T) {
        lateinit var next: Node<T>
        lateinit var prev: Node<T>

        override fun toString(): String {
            return "Node(value=$value, prev=${prev.value}, next=${next.value})"
        }

        fun remove() {
            prev.next = next
            next.prev = prev
            //Because we cache the nodes, it is safer to clear out the next / prev references.
            next = this
            prev = this
        }
    }

    companion object {
        fun <T> ofValues(values: Iterable<T>): GenericCircularList<T> {
            val iterator = values.iterator()
            if (!iterator.hasNext()) {
                throw IllegalArgumentException("Empty collections not supported")
            }
            val head = Node(iterator.next())
            var previous = head
            while (iterator.hasNext()) {
                val value = iterator.next()
                val newNode = Node(value)
                newNode.prev = previous
                previous.next = newNode
                previous = newNode
            }
            previous.next = head
            head.prev = previous
            return GenericCircularList(head)
        }
    }

}