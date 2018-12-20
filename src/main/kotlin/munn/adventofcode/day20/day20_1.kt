package munn.adventofcode.day20

import java.io.File

fun main(args: Array<String>) {

    val input = File(args.first()).readText().drop(1).dropLast(1)

    var index = 0
    fun parseInput(): Int {
        val sections = mutableListOf(0)
        while (index < input.length && input[index] != ')') {
            val c = input[index]
            if (c == '|') {
                sections.add(0)
            } else if (c == '(') {
                index++
                sections[sections.lastIndex] += parseInput()
            } else {
                sections[sections.lastIndex]++
            }
            index++
        }
        val moreAfter = input.getOrNull(index + 1) ?: ')' !in setOf(')', '|')
        return if (moreAfter) sections.min()!! else sections.max()!!
    }

    println(parseInput())
}