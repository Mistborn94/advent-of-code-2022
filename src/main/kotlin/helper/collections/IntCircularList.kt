package helper.collections

/**
 * Circular list containing unique positive integer values
 */
class IntCircularList(val size: Int, elements: Collection<Int> = emptyList()) {

    private val nextLinks = IntArray(size) { UNASSIGNED }
    private val prevLinks = IntArray(size) { UNASSIGNED }

    init {
        var previous = elements.first()
        elements.forEach { new ->
            addAfter(previous, new)
            previous = new
        }
    }

    fun getNext(value: Int): Int = nextLinks[value]

    operator fun contains(value: Int) = nextLinks[value] != UNASSIGNED

    fun remove(value: Int): Int {
        val next = nextLinks[value]
        val prev = prevLinks[value]
        link(prev, next)
        nextLinks[value] = UNASSIGNED
        prevLinks[value] = UNASSIGNED
        return value
    }

    fun addAfter(before: Int, element: Int) {
        val oldNext = getNext(before)
        link(before, element)

        if (oldNext == UNASSIGNED) {
            link(element, before)
        } else {
            link(element, oldNext)
        }
    }

    fun addAfter(before: Int, elements: Collection<Int>) {
        var previous = before
        elements.forEach { new ->
            addAfter(previous, new)
            previous = new
        }
    }

    private fun link(previous: Int, next: Int) {
        nextLinks[previous] = next
        prevLinks[next] = previous
    }

    companion object {
        private const val UNASSIGNED = -1
    }

}

