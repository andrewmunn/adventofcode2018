package munn.adventofcode.day6

import munn.adventofcode.utils.Point
import java.io.File

fun main(args: Array<String>) {
    val points = File(args.first()).readLines().map { it.split(',') }
        .map { Point(it.first().trim().toInt(), it.last().trim().toInt()) }

    val maxX = points.maxBy { it.x }!!.x + 100 // add 100 to the edge in case valid area is outside
    val maxY = points.maxBy { it.y }!!.y + 100

    val grid = IntArray(maxX * maxY) { i ->
        val x = i / maxY
        val y = i % maxY
        points.sumBy { point -> Math.abs(point.x - x) + Math.abs(point.y - y) }
    }

    println(grid.count { it < 10000 })
}