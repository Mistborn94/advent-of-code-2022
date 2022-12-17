package helper.collections

interface PeekingIterator<T> : Iterator<T> {
    fun peek(): T
}