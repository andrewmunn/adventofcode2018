package munn.adventofcode.day12

import java.io.File

fun main(args: Array<String>) {

    val plants = File(args.first()).readLines().first().map { it == '#' }.mapIndexed { index, isPlant -> index.toLong() to isPlant }.toMap()

    val rules = File(args.first()).readLines().drop(2).map { line ->
        val sections = line.split(" => ")
        Rule(sections.first().map { it == '#' }, sections.last().single() == '#')
    }.also { check(it.size == 32) }

    println(plants)
    println(rules)

    var gen = plants
    (1..20).forEach { i ->
        val lowestEntry = gen.entries.minBy { it.key }!!
        if (lowestEntry.value) {
            gen += mapOf(lowestEntry.key - 1 to false, lowestEntry.key - 2 to false)
        } else if (gen[lowestEntry.key + 1]!!) {
            gen += lowestEntry.key - 1 to false
        }
        val highestEntry = gen.entries.maxBy { it.key }!!
        if (highestEntry.value) {
            gen += mapOf(highestEntry.key + 1 to false, highestEntry.key + 2 to false)
        } else if (gen[highestEntry.key - 1]!!) {
            gen += highestEntry.key + 1 to false
        }
        gen = gen.mapValues { entry -> rules.mapNotNull { rule -> rule.matchResult(gen, entry.key) }.single() }
    }

    println(gen.filterValues { it }.keys.sum())
    println(gen)
}