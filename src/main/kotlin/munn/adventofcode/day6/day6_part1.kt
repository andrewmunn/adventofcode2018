package munn.adventofcode.day6

import java.io.File

fun main(args: Array<String>) {
    val points = File(args.first()).readLines()
        .map { it.split(',') }
        .map { Point(it.first().trim().toInt(), it.last().trim().toInt()) }

    val maxX = points.maxBy { it.x }!!.x + 1
    val maxY = points.maxBy { it.y }!!.y + 1

    val grid = Array(maxX) { Array(maxY) { Score() } }

    points.forEach { point ->
        grid.indices.forEach { x ->
            grid[x].indices.forEach { y ->
                val score = grid[x][y]
                val distance = Math.abs(point.x - x) + Math.abs(point.y - y)
                if (score.distance > distance) {
                    grid[x][y] = Score(point, distance)
                } else if (score.distance == distance) {
                    grid[x][y] = Score(null, distance)
                }
            }
        }
    }

    val infinitePoints: Set<Point> = ((grid.first() + grid.last())
        .map { it.point } + grid.map { it.first().point } + grid.map { it.last().point })
        .filterNotNull().toSet()

    val max = grid.flatMap { it.toList() }
        .mapNotNull { it.point }
        .groupingBy { it}
        .eachCount()
        .filter { it.key !in infinitePoints }
        .maxBy { it.value }

    println(max)
}

data class Point(val x: Int, val y: Int)

data class Score(val point: Point? = null, val distance: Int = Int.MAX_VALUE)