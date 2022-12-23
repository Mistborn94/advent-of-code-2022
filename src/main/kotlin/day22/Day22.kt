package day22

import day22.Direction.*
import day22.Orientation.HORIZONTAL
import day22.Orientation.VERTICAL
import helper.point.*

enum class Orientation {
    HORIZONTAL,
    VERTICAL
}

enum class Direction(val point: Point, val char: Char, val orientation: Orientation) {

    UP(Point(0, -1), '^', VERTICAL) {
        override val left get() = LEFT
        override val right get() = RIGHT
        override val opposite get() = DOWN
    },
    LEFT(Point(-1, 0), '<', HORIZONTAL) {
        override val left get() = DOWN
        override val right get() = UP
        override val opposite get() = RIGHT
    },
    DOWN(Point(0, 1), 'v', VERTICAL) {
        override val left get() = RIGHT
        override val right get() = LEFT
        override val opposite get() = UP
    },
    RIGHT(Point(1, 0), '>', HORIZONTAL) {
        override val left get() = UP
        override val right get() = DOWN
        override val opposite get() = LEFT
    };

    abstract val left: Direction
    abstract val right: Direction
    abstract val opposite: Direction
}

//Follow the path given in the monkeys' notes. What is the final password?
//A number indicates the number of tiles to move in the direction you are facing.
//A letter indicates whether to turn 90 degrees clockwise (R) or counterclockwise (L)
//The final password is the sum of 1000 times the row, 4 times the column, and the facing.
fun solveA(text: String): Int {
    val board = parseBoard(text.lines().dropLast(2))
    val list = text.lines().last().replace("L", " L ").replace("R", " R ").split(" ")
    val instructions = list.iterator()

    val startingPosition = board.indexOf('.')
    val startingDirection = RIGHT

    var currentPosition = startingPosition
    var currentDirection = startingDirection

    val path = mutableListOf<Pair<Point, Direction>>()

//
//    println("Starting Board: ")
//    println(board.joinToString("\n") { it.joinToString(separator = "", prefix = "|", postfix = "|")})

    path.add(currentPosition to currentDirection)
    while (instructions.hasNext()) {
        val next = instructions.next()
        val nextInt = next.toIntOrNull()
        if (nextInt != null) {
            for (i in 0 until nextInt) {
                val nextPos = currentPosition + currentDirection.point
                if (nextPos !in board || board[nextPos] == ' ') {
                    val offBoard = when (currentDirection) {
                        UP -> Point(currentPosition.x, board.size)
                        DOWN -> Point(currentPosition.x, -1)
                        LEFT -> Point(board[0].size, currentPosition.y)
                        RIGHT -> Point(-1, currentPosition.y)
                    }
                    var possibleNext = offBoard + currentDirection.point
                    while (board[possibleNext] == ' ') {
                        possibleNext += currentDirection.point
                    }
                    if (board[possibleNext] == '.') {
                        currentPosition = possibleNext
                    }
                } else if (board[nextPos] == '#') {
                    break
                } else {
                    currentPosition = nextPos
                }
                path.add(currentPosition to currentDirection)
            }
            //do move
        } else if (next == "L") {
            currentDirection = currentDirection.left
            path.add(currentPosition to currentDirection)
        } else if (next == "R") {
            currentDirection = currentDirection.right
            path.add(currentPosition to currentDirection)
        }
//        println("Finished instruction $next, position is $currentPosition, $currentDirection")
//        printPath(board, path)
    }


    val facingScore = when (currentDirection) {
        RIGHT -> 0
        DOWN -> 1
        LEFT -> 2
        UP -> 3
    }

//    println("End is row=${currentPosition.y + 1}, col=${currentPosition.x + 1} $currentDirection:")
    return (currentPosition.y + 1) * 1000 + (currentPosition.x + 1) * 4 + facingScore
}

private fun parseBoard(textLines: List<String>): List<List<Char>> {
    val longestLine = textLines.maxOf { it.length }
    return textLines.map { it.padEnd(longestLine).toList() }
}

private fun printPath(board: List<List<Char>>, path: MutableList<Pair<Point, Direction>>) {
    val mutableBoard = board.map { it.toMutableList() }
    path.forEach { (point, direction) -> mutableBoard[point] = direction.char }
    mutableBoard[path.last().first] = '*'
    println(mutableBoard.joinToString("\n") { it.joinToString(separator = "", prefix = "|", postfix = "|") })
}

//Encodes the transitions between faces
val problemSets: Map<String, Map<Int, Map<Direction, Pair<Int, Direction>>>> = mapOf(
    "sample" to mapOf(
        1 to mapOf(
            DOWN to (4 to DOWN),
            UP to (2 to DOWN),
            LEFT to (3 to DOWN),
            RIGHT to (6 to LEFT)
        ),
        2 to mapOf(
            UP to (1 to DOWN),
            DOWN to (5 to UP),
            LEFT to (6 to UP),
            RIGHT to (3 to RIGHT),
        ),
        3 to mapOf(
            UP to (1 to RIGHT),
            DOWN to (5 to RIGHT),
            LEFT to (2 to LEFT),
            RIGHT to (4 to RIGHT)
        ),
        4 to mapOf(
            UP to (1 to UP),
            DOWN to (5 to DOWN),
            LEFT to (3 to LEFT),
            RIGHT to (6 to DOWN),
        ),
        5 to mapOf(
            UP to (4 to UP),
            LEFT to (3 to UP),
            DOWN to (2 to UP),
            RIGHT to (6 to RIGHT)
        ),
        6 to mapOf(
            UP to (4 to LEFT),
            RIGHT to (1 to LEFT),
            DOWN to (2 to RIGHT),
            LEFT to (5 to LEFT)
        )
    ),
    "me" to mapOf(
        1 to mapOf(
            DOWN to (3 to DOWN),
            UP to (6 to RIGHT),
            LEFT to (4 to RIGHT),
            RIGHT to (2 to RIGHT)
        ),
        2 to mapOf(
            UP to (6 to UP),
            DOWN to (3 to LEFT),
            LEFT to (1 to LEFT),
            RIGHT to (5 to LEFT),
        ),
        3 to mapOf(
            UP to (1 to UP),
            DOWN to (5 to DOWN),
            LEFT to (4 to DOWN),
            RIGHT to (2 to UP)
        ),
        4 to mapOf(
            UP to (3 to RIGHT),
            DOWN to (6 to DOWN),
            LEFT to (1 to RIGHT),
            RIGHT to (5 to RIGHT),
        ),
        5 to mapOf(
            UP to (3 to UP),
            LEFT to (4 to LEFT),
            DOWN to (6 to LEFT),
            RIGHT to (2 to LEFT)
        ),
        6 to mapOf(
            UP to (4 to UP),
            RIGHT to (5 to UP),
            DOWN to (2 to DOWN),
            LEFT to (1 to DOWN)
        )
    )
)
typealias CubeFace = List<List<Char>>

fun solveB(text: String, cubeSize: Int = 50, problemName: String = "me"): Int {
    val cubeMap = problemSets[problemName]!!

    val board = parseBoard(text.lines().dropLast(2))
    val cubeFaces = parseCube(cubeSize, text.lines().dropLast(2))

    val list = text.lines().last().replace("L", " L ").replace("R", " R ").split(" ")
    val instructions = list.iterator()

    val startingDirection = RIGHT

    var currentPosition = Point(0, 0)
    var currentDirection = startingDirection
    var currentFaceId = 1
    var currentFace = cubeFaces[currentFaceId]!!.first
    var currentFaceOffset = cubeFaces[currentFaceId]!!.second

    val path = mutableListOf<Pair<Point, Direction>>()
    path.add(currentPosition.toGlobalCoord(currentFaceOffset, cubeSize) to currentDirection)
    while (instructions.hasNext()) {
        val next = instructions.next()
        val nextInt = next.toIntOrNull()
        if (nextInt != null) {
            for (i in 0 until nextInt) {
                val nextPos = currentPosition + currentDirection.point
                if (nextPos !in currentFace) {
                    //do wrap around
                    val currentFaceMap = cubeMap[currentFaceId]!!
                    val (nextFaceId, nextDirection) = currentFaceMap[currentDirection]!!
                    val (nextFace, nextFaceOffset) = cubeFaces[nextFaceId]!!
                    val constantComponent = when (nextDirection) {
                        RIGHT, DOWN -> 0
                        LEFT, UP -> cubeSize - 1
                    }

                    val nextFacePos = when (nextDirection.orientation) {
                        HORIZONTAL -> {
                            val y = when (nextDirection) {
                                currentDirection -> currentPosition.y
                                currentDirection.opposite -> cubeSize - currentPosition.y - 1
                                currentDirection.left -> cubeSize - currentPosition.x - 1
                                currentDirection.right -> currentPosition.x
                                else -> throw IllegalStateException("Impossible")
                            }
                            Point(constantComponent, y)
                        }

                        VERTICAL -> {
                            val x = when (nextDirection) {
                                currentDirection -> currentPosition.x
                                currentDirection.opposite -> cubeSize - currentPosition.x - 1
                                currentDirection.left -> currentPosition.y
                                currentDirection.right -> cubeSize - currentPosition.y - 1
                                else -> throw IllegalStateException("Impossible")
                            }
                            Point(x, constantComponent)
                        }
                    }

                    if (nextFace[nextFacePos] == '#') {
                        break
                    } else {
                        currentPosition = nextFacePos
                        currentFace = nextFace
                        currentFaceId = nextFaceId
                        currentFaceOffset = nextFaceOffset
                        currentDirection = nextDirection
                    }

                } else if (currentFace[nextPos] == '#') {
                    break
                } else {
                    currentPosition = nextPos
                }
                path.add(currentPosition.toGlobalCoord(currentFaceOffset, cubeSize) to currentDirection)
            }
        } else if (next == "L") {
            currentDirection = currentDirection.left
            path.add(currentPosition.toGlobalCoord(currentFaceOffset, cubeSize) to currentDirection)
        } else if (next == "R") {
            currentDirection = currentDirection.right
            path.add(currentPosition.toGlobalCoord(currentFaceOffset, cubeSize) to currentDirection)
        }
//        println("Finished instruction $next, position is $currentPosition, $currentDirection:")
//        printPath(board, path)
    }

    val facingScore = when (currentDirection) {
        RIGHT -> 0
        DOWN -> 1
        LEFT -> 2
        UP -> 3
    }
    val global = currentPosition.toGlobalCoord(currentFaceOffset, cubeSize)
    return (global.y + 1) * 1000 + (global.x + 1) * 4 + facingScore
}

fun Point.toGlobalCoord(offset: Point, cubeSize: Int): Point = Point(x + offset.x * cubeSize, y + offset.y * cubeSize)

private fun parseCube(cubeSize: Int, textLines: List<String>): MutableMap<Int, Pair<CubeFace, Point>> {
    val longestLine = textLines.maxOf { it.length }
    val cubeCount = longestLine / cubeSize
    val horizontalSlices = textLines.map { it.padEnd(longestLine) }
        .chunked(cubeSize)

    val possibleCubeFaces: List<List<CubeFace>> = horizontalSlices.map { lines ->
        val lineChunks = lines.map { it.toList().chunked(cubeSize) }
        (0 until cubeCount).map { cubeId ->
            lineChunks.map { it[cubeId] }
        }
    }

    var currentFace = 1
    val cubeFaces = mutableMapOf<Int, Pair<CubeFace, Point>>()
    possibleCubeFaces.forEachIndexed { row, slice ->
        slice.forEachIndexed { column, face ->
            if (face.first().first() != ' ') {
                cubeFaces[currentFace] = face to Point(column, row)
                currentFace += 1
            }
        }
    }
    return cubeFaces
}
