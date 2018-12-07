package munn.adventofcode.day7

import java.io.File

fun main(args: Array<String>) {

    val stepMap = mutableMapOf<Char, Step>()
    val regex = Regex("""Step ([A-Z]) must be finished before step ([A-Z]) can begin.""")

    File(args.first()).readLines().sorted().forEach { line ->
        val values = regex.matchEntire(line)!!.groupValues
        val stepName = values[2].single()
        val dependsOnName = values[1].single()
        val step = stepMap.getOrElse(values[2].single()) { Step(stepName) }
        val dependsOnStep = stepMap.getOrElse(dependsOnName) { Step(dependsOnName) }
        step.dependsOn.add(dependsOnName)
        stepMap.putIfAbsent(stepName, step)
        stepMap.putIfAbsent(dependsOnName, dependsOnStep)
    }

    while (stepMap.isNotEmpty()) {
        val nextStep = stepMap.values.filter { it.dependsOn.isEmpty() }.sortedBy { it.step }.first()
        print(nextStep.step)
        stepMap.remove(nextStep.step)
        stepMap.values.forEach { it.dependsOn.remove(nextStep.step) }
    }
}

private data class Step(val step: Char, val dependsOn: MutableSet<Char> = mutableSetOf())
