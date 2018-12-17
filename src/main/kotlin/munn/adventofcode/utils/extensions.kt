package munn.adventofcode.utils

import java.util.*

fun String.mutate(block: StringBuilder.() -> Unit): String = StringBuilder(this).apply(block).toString()

fun List<*>.rotate(distance: Int) = Collections.rotate(this, distance)

fun <T> buildList(block: MutableList<T>.() -> Unit): List<T> = mutableListOf<T>().apply(block)