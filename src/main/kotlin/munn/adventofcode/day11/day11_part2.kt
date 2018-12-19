package munn.adventofcode.day11

import munn.adventofcode.utils.Point

fun main(args: Array<String>) {
    val input = 5719L
    val grid = Array(300) { x ->
        IntArray(300) { y ->
            val rackId = (x + 1) + 10L
            val value = (((rackId * (y + 1) + input) * rackId).toString().toInt() / 100 % 10) - 5
            println(value)
            value
        }
    }

    var max = Int.MIN_VALUE
    var maxPoint = Pair(Point(0, 0), 0)
    (1..300).forEach { size ->
        grid.dropLast(size - 1).indices.forEach { x ->
            grid[x].dropLast(size - 1).indices.forEach { y ->
                var level = 0
                (0 until size).forEach { dx ->
                    (0 until size).forEach { dy ->
                        level += grid[x + dx][y + dy]
                    }
                }
                if (level > max) {
                    max = level
                    maxPoint = Point(x + 1, y + 1) to size
                }
            }
        }
        println("Completed size: $size")
        println("max so far: $maxPoint with $max")
    }


    println(maxPoint)
    println(max)
}