package munn.adventofcode.utils

fun String.mutate(block: StringBuilder.() -> Unit): String = StringBuilder(this).apply(block).toString()