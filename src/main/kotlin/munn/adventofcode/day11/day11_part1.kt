package munn.adventofcode.day11

import munn.adventofcode.utils.Point

fun main(args: Array<String>) {
    val input = 5719L
    val grid = Array(300) { x ->
        IntArray(300) { y ->
            val rackId = (x + 1) + 10L
            (((rackId * (y + 1) + input) * rackId).toString().reversed().getOrNull(2)?.toInt() ?: 0) - 5
        }
    }

    var max = Int.MIN_VALUE
    var maxPoint = Point(0, 0)
    grid.dropLast(2).indices.forEach { x ->
        grid[x].dropLast(2).indices.forEach { y ->
            val level = grid[x][y] + grid[x + 1][y] + grid[x + 2][y] +
                    grid[x][y + 1] + grid[x + 1][y + 1] + grid[x + 2][y + 1] +
                    grid[x][y + 2] + grid[x + 1][y + 2] + grid[x + 2][y + 2]
            if (level > max) {
                max = level
                maxPoint = Point(x + 1, y + 1)
            }
        }
    }

    println(maxPoint)
    println(max)
}