package munn.adventofcode.day18

import java.io.File

fun main(args: Array<String>) {
    var before = File(args.first()).readLines().map { it.toList() }
    val results = mutableListOf<Int>()
    repeat(10001) { i ->

        if (i >= 9900) {
            val flattened = before.flatten()
            val woods = flattened.count { it == '|' }
            val yards = flattened.count { it == '#' }
            results.add(woods * yards)
        }
        val after = List(before.size) { x ->
            List(before[x].size) { y ->
                val tile = before[x][y]
                val adjacent = before.adjacent(x, y)
                when (tile) {
                    '.' -> if (adjacent.count { it == '|' } >= 3) '|' else '.'
                    '|' -> if (adjacent.count { it == '#' } >= 3) '#' else '|'
                    '#' -> if (adjacent.count { it == '#' } >= 1 && adjacent.count { it == '|' } >= 1) '#' else '.'
                    else -> error("Unexpected input $tile")
                }
            }
        }
        before = after
    }

    results.forEachIndexed { i, result ->
        println("${9900 + i}: $result")
    }

}

fun <T : Any> List<List<T>>.adjacent(x: Int, y: Int): List<T> {
    return listOfNotNull(
        getOrNull(x - 1)?.getOrNull(y - 1),
        getOrNull(x)?.getOrNull(y - 1),
        getOrNull(x + 1)?.getOrNull(y - 1),
        getOrNull(x - 1)?.getOrNull(y),
        getOrNull(x + 1)?.getOrNull(y),
        getOrNull(x - 1)?.getOrNull(y + 1),
        getOrNull(x)?.getOrNull(y + 1),
        getOrNull(x + 1)?.getOrNull(y + 1)
    )
}