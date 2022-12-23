package day23

import helper.point.Point

val north = Point(0, -1)
val east = Point(-1, 0)
val south = Point(0, 1)
val west = Point(1, 0)

val surroundingOffsets = listOf(
    north,
    north + east,
    east,
    east + south,
    south,
    south + west,
    west,
    west + north
)

val globalRule = listOf(Rule("Surrounding", surroundingOffsets, Point(0, 0)))
val directionRules = listOf(
    rule("North", north, east, west),
    rule("South", south, east, west),
    rule("West", east, north, south),
    rule("East", west, north, south),
)

//Simulate the Elves' process and find the smallest rectangle that contains the Elves
// after 10 rounds. How many empty ground tiles does that rectangle contain?
fun solveA(text: String, repeat: Int = 10): Int {
    /*
     If no other Elves are in one of those eight positions, the Elf does not do anything
    If there is no Elf in the N, NE, or NW adjacent positions, the Elf proposes moving north one step.
    If there is no Elf in the S, SE, or SW adjacent positions, the Elf proposes moving south one step.
    If there is no Elf in the W, NW, or SW adjacent positions, the Elf proposes moving west one step.
    If there is no Elf in the E, NE, or SE adjacent positions, the Elf proposes moving east one step.
*/
    var elves: Set<Elf> = parseElves(text)

//    println("Start")
//    printBoard(elves)
    val lastMatchedRules = mutableMapOf<Int, Int>()

    repeat(repeat) { iteration ->
        val (newElves, _) = simulateRound(iteration, elves)
        elves = newElves
    }

    val minX = elves.minOf { it.position.x }
    val maxX = elves.maxOf { it.position.x }

    val minY = elves.minOf { it.position.y }
    val maxY = elves.maxOf { it.position.y }

    return (maxX - minX + 1) * (maxY - minY + 1) - elves.size
}

private fun parseElves(text: String): Set<Elf> {
    var id = 0
    return text.lines().flatMapIndexedTo(mutableSetOf()) { r, line ->
        line.mapIndexedNotNull { c, e ->
            if (e == '#') Elf(id++, Point(c, r)) else null
        }
    }
}

private fun simulateRound(
    round: Int,
    elves: Set<Elf>
): Pair<MutableSet<Elf>, Int> {
    val firstIterationRule = round % 4
    val iterationRules = globalRule + if (firstIterationRule == 0) directionRules else directionRules.subList(firstIterationRule, directionRules.size) + directionRules.subList(0, firstIterationRule)

    val elfPositions = elves.mapTo(mutableSetOf()) { it.position }
    val nextMoves = elves.associateWith { elf -> iterationRules.firstNotNullOfOrNull { it.nextMove(elf, elfPositions) } ?: elf.position }
    val nextMoveCounts = nextMoves.values.groupingBy { it }.eachCount()

    var moveCount = 0
    val nextElves = elves.mapTo(mutableSetOf()) { elf ->
        val nextMove = nextMoves[elf]!!
        val count = nextMoveCounts[nextMove]!!
        if (count == 1 && nextMove != elf.position) {
            moveCount++
            elf.moveTo(nextMove)
        } else {
            elf
        }
    }

    return nextElves to moveCount
}

fun printBoard(elves: Set<Elf>) {
    val elfPositions = elves.mapTo(mutableSetOf()) { it.position }
    val minX = elfPositions.minOf { it.x } - 1
    val maxX = elfPositions.maxOf { it.x } + 1

    val minY = elfPositions.minOf { it.y } - 1
    val maxY = elfPositions.maxOf { it.y } + 1

    println(buildString {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                val point = Point(x, y)
                if (point in elfPositions) {
                    append('#')
                } else {
                    append('.')
                }
            }
            append("\n")
        }
    })
}

private fun rule(name: String, first: Point, o1: Point, o2: Point) = Rule(name, listOf(first, first + o1, first + o2), first)

fun solveB(text: String): Int {
    var elves: Set<Elf> = parseElves(text)

    var iteration = 0

    do {
        val (newElves, moveCount) = simulateRound(iteration++, elves)
        elves = newElves
    } while (moveCount != 0)

    return iteration
}


data class Elf(val id: Int, val position: Point) {
    fun moveTo(new: Point): Elf {
        return if (new == position) {
            this
        } else {
            Elf(id, new)
        }
    }
}

class Rule(private val name: String, private val locationOffsets: Collection<Point>, private val moveOffset: Point) {

    fun nextMove(elf: Elf, otherElves: Set<Point>): Point? {
        return if (locationOffsets.none { (it + elf.position) in otherElves }) {
            elf.position + moveOffset
        } else {
            null
        }
    }

    override fun toString(): String {
        return "Rule(name='$name')"
    }
}