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

    val knownInners = mutableSetOf<Point3D>()
    val knownOuters = mutableSetOf<Point3D>()
    points.asSequence()
        .flatMap { it.openNeighboursInBox(boundingBox, points).asSequence() }
        .distinct()
        .forEach { possible ->
            if (possible !in knownInners && possible !in knownOuters) {
                val path = findShortestPath(possible, end, { point -> point.openNeighboursInBox(biggerBox, points) }, heuristic = { (end - it).abs() })
                if (path.end == null) {
                    knownInners.add(possible)
                    knownInners.addAll(path.seen())
                } else {
                    knownOuters.add(possible)
                    knownOuters.addAll(path.seen())
                }
            }
        }
    return knownInners
}

private fun Point3D.openNeighboursInBox(boundingBox: Cube, points: Set<Point3D>) = neighbours().filter { neighbour -> neighbour in boundingBox && neighbour !in points }