package helper.point

enum class Direction(val point: Point) {

    NORTH(Point(0, 1)) {
        override val right get() = EAST
        override val left get() = WEST
    },
    EAST(Point(1, 0)) {
        override val right get() = SOUTH
        override val left get() = NORTH
    },
    SOUTH(Point(0, -1)) {
        override val right get() = WEST
        override val left get() = EAST
    },
    WEST(Point(-1, 0)) {
        override val right get() = NORTH
        override val left get() = SOUTH
    };

    abstract val right: Direction
    abstract val left: Direction

    fun left(times: Int): Direction {
        return if (times == 1) {
            left
        } else {
            left.left(times - 1)
        }
    }

    fun right(times: Int): Direction {
        return if (times == 1) {
            right
        } else {
            right.right(times - 1)
        }
    }
}