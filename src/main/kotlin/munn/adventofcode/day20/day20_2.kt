package munn.adventofcode.day20

import munn.adventofcode.utils.Point
import java.io.File
import java.util.*

fun main(args: Array<String>) {

    val input = File(args.first()).readText().drop(1).dropLast(1)

    var index = 0
    val points = mutableSetOf<Point>()
    val edges = mutableMapOf<Point, MutableSet<Point>>()
    fun addPoint(point: Point, edgeTo: Point) {
        points.add(point)
        edges.getOrPut(point) { mutableSetOf() }.add(edgeTo)
        edges.getOrPut(edgeTo) { mutableSetOf() }.add(point)
    }

    fun parseInput(start: Point): Point {
        val sections = mutableListOf(start)
        while (index < input.length && input[index] != ')') {
            val c = input[index]
            val lastPoint = sections[sections.lastIndex]
            when (c) {
                '|' -> sections.add(start)
                '(' -> {
                    index++
                    sections[sections.lastIndex] = parseInput(lastPoint)
                }
                'E' -> sections[sections.lastIndex] = lastPoint.dX(-1).also { addPoint(it, lastPoint) }
                'N' -> sections[sections.lastIndex] = lastPoint.dY(-1).also { addPoint(it, lastPoint) }
                'W' -> sections[sections.lastIndex] = lastPoint.dX(1).also { addPoint(it, lastPoint) }
                'S' -> sections[sections.lastIndex] = lastPoint.dY(1).also { addPoint(it, lastPoint) }
            }
            index++
        }
        return sections.firstOrNull() ?: start
    }

    val origin = Point(0, 0)
    points.add(origin)
    parseInput(origin)

    val queue = PriorityQueue<Distance>()
    val distances = points.associateTo(mutableMapOf()) { it to Distance(it, Int.MAX_VALUE) }
    val nodesVisited = mutableSetOf<Point>()
    distances[origin] = Distance(origin, 0)
    points.forEach { queue.add(distances[it]!!) }
    while (queue.isNotEmpty()) {
        val current = queue.remove()
        val neighbors = edges[current.point]!!
        for (neighbor in neighbors) {
            val existingDistance = distances[neighbor]!!
            val newDistance = Distance(neighbor, current.value + 1)
            if (newDistance < existingDistance) {
                distances[neighbor] = newDistance
                if (neighbor !in nodesVisited) {
                    check(queue.remove(existingDistance))
                    queue.add(newDistance)
                }
            }
        }
        nodesVisited.add(current.point)
    }

    println(distances.maxBy { it.value.value })
    println(distances.count { it.value.value >= 1000 })
}

data class Distance(
    val point: Point,
    val value: Int) : Comparable<Distance> {

    override fun compareTo(other: Distance): Int {
        return value - other.value
    }
}