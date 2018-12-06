package munn.adventofcode.day6

import java.io.File

fun main(args: Array<String>) {
    val points = File(args.first()).readLines()
        .map { it.split(',') }
        .map { Point(it.first().trim().toInt(), it.last().trim().toInt()) }

    val maxX = points.maxBy { it.x }!!.x + 100
    val maxY = points.maxBy { it.y }!!.y + 100

    val grid = Array(maxX) { IntArray(maxY) }

    points.forEach { point ->
        grid.indices.forEach { x ->
            grid[x].indices.forEach { y ->
                grid[x][y] += Math.abs(point.x - x) + Math.abs(point.y - y)
            }
        }
    }

    val valid = grid.flatMap { it.toList() }
        .filter { it < 10000 }
        .count()

    println(valid)
}