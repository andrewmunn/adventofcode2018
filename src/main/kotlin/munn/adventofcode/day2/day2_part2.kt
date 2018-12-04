package munn.adventofcode.day2

import java.io.File

fun main(args: Array<String>) {
    val lines = File(args.first()).readLines()
    repeat(lines.first().length) { index ->
        val dupes = mutableSetOf<String>()
        lines.map { it.removeRange(index, index + 1) }
            .forEach {
                if (!dupes.add(it)) {
                    println("Found match: $it")
                }
            }
    }
}