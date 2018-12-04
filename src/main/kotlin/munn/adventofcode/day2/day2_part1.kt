package munn.adventofcode.day2

import java.io.File

fun main(args: Array<String>) {
    val lines =  File(args.first()).readLines()
    val exactly2Count = lines
        .filter { id -> id.groupBy { it }.values.any { it.size == 2 } }
        .count()

    val exactly3Count = lines
        .filter { id -> id.groupBy { it }.values.any { it.size == 3 } }
        .count()

    println("Checksum: ${exactly2Count * exactly3Count}")
}