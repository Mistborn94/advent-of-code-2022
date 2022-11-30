package helper.collections

/**
 * Circular list containing unique values
 */
class GenericCircularList<T>(var head: Node<T>, private val nodes: MutableMap<T, Node<T>>) {

    fun getNode(value: T) = nodes.getValue(value)

    fun remove(node: Node<T>): T {
        node.remove()
        return node.value
    }

    fun addAfter(value: T, elements: Collection<T>) = addAfter(nodes.getValue(value), elements)
    fun addAfter(startNode: Node<T>, elements: Collection<T>) {
        elements.iterator()
        val end = startNode.next
        var previous = startNode
        elements.forEach { value ->
            val newNode = nodes.getOrPut(value) { Node(value) }
            newNode.prev = previous
            previous.next = newNode
            previous = newNode
        }
        previous.next = end
        end.prev = previous
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

    fun shiftLeft() {
        head = head.next
    }

    companion object {
        fun <T> ofValues(values: Collection<T>, nodes: MutableMap<T, Node<T>>): GenericCircularList<T> {
            if (values.isEmpty()) {
                throw IllegalArgumentException("Empty collections not supported")
            }
            val iterator = values.iterator()
            val head = Node(iterator.next())
            nodes[head.value] = head
            var previous = head
            while (iterator.hasNext()) {
                val value = iterator.next()
                val newNode = Node(value)
                newNode.prev = previous
                previous.next = newNode
                previous = newNode
                nodes[value] = newNode
            }
            previous.next = head
            head.prev = previous
            return GenericCircularList(head, nodes)
        }
    }

}