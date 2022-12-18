package day18

import helper.graph.findShortestPath
import helper.point.Cube
import helper.point.Point3D

//What is the surface area of your scanned lava droplet?
fun solveA(text: String): Int {

    val points = text.lines().mapTo(mutableSetOf()) { line ->
        val (xs, ys, zs) = line.split(",")
        Point3D(xs.toInt(), ys.toInt(), zs.toInt())
    }

    return points.sumOf { 6 - sharedSides(it, points) }
}

fun solveB(text: String): Int {

    val points = text.lines().mapTo(mutableSetOf()) { line ->
        val (xs, ys, zs) = line.split(",")
        Point3D(xs.toInt(), ys.toInt(), zs.toInt())
    }

    val boundingBox = Cube.boundingBoxOf(points)

    val knownInners = findInnerCubes(boundingBox, points)

    return points.sumOf { 6 - sharedSides(it, points) - sharedSides(it, knownInners) }
}

fun sharedSides(point: Point3D, points: Set<Point3D>): Int {
    return point.neighbours().count { it in points }
}

private fun findInnerCubes(boundingBox: Cube, points: Set<Point3D>): MutableSet<Point3D> {
    val end = Point3D(boundingBox.xRange.first - 1, boundingBox.yRange.first - 1, boundingBox.zRange.first - 1)
    val biggerBox = boundingBox.grow()
    val possibleInners = points.flatMapTo(mutableSetOf()) { it.openNeighboursInBox(boundingBox, points) }
    val knownInners = mutableSetOf<Point3D>()
    possibleInners.forEach { possible ->
        if (possible !in knownInners) {
            val path = findShortestPath(possible, end, { point -> point.openNeighboursInBox(biggerBox, points) }, heuristic = { (end - it).abs() })
            if (path == null) {
                knownInners.add(possible)
                knownInners.addAll(innerArea(possible, points))
            }
        }
    }
    return knownInners
}

private fun Point3D.openNeighboursInBox(boundingBox: Cube, points: Set<Point3D>) = neighbours().filter { neighbour -> neighbour in boundingBox && neighbour !in points }

fun innerArea(start: Point3D, points: Set<Point3D>): Collection<Point3D> {
    val toVisit = LinkedHashSet<Point3D>()
    toVisit.add(start)
    val area = mutableSetOf<Point3D>()

    while (toVisit.isNotEmpty()) {
        val current = toVisit.first()
        toVisit.remove(current)
        if (area.add(current)) {
            toVisit.addAll(current.neighbours().filter { it !in points })
        }
    }

    return area
}

