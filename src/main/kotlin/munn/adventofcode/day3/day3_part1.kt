package munn.adventofcode.day3

import java.io.File

fun main(args: Array<String>) {
    val regex = Regex("""#([0-9]+) @ ([0-9]+),([0-9]+): ([0-9]+)x([0-9]+)""")
    val claims = File(args.first()).readLines().map { line ->
        val values = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        Claim(
            id = values[0],
            x = values[1],
            y = values[2],
            w = values[3],
            h = values[4]
        )
    }

    val matrix = Array(1000) { IntArray(1000) }
    claims.forEach { c ->
        repeat(c.w) { i ->
            repeat(c.h) { j ->
                matrix[c.x + i][c.y + j] += 1
            }
        }
    }

    var sum = 0
    matrix.forEach { row ->
       row.forEach { square ->
           if (square > 1) {
               sum++
           }
       }
    }

    println(sum)
}

private data class Claim(
    val id: Int,
    val x: Int,
    val y: Int,
    val w: Int,
    val h: Int
)