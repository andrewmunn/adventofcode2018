package munn.adventofcode.day7

import java.io.File

fun main(args: Array<String>) {

    val stepMap = mutableMapOf<Char, StepWithTime>()
    val regex = Regex("""Step ([A-Z]) must be finished before step ([A-Z]) can begin.""")

    File(args.first()).readLines().sorted().forEach { line ->
        val values = regex.matchEntire(line)!!.groupValues
        val stepName = values[2].single()
        val dependsOnName = values[1].single()
        val step = stepMap.getOrElse(values[2].single()) { StepWithTime(stepName) }
        val dependsOnStep = stepMap.getOrElse(dependsOnName) { StepWithTime(dependsOnName) }
        step.dependsOn.add(dependsOnName)
        stepMap.putIfAbsent(stepName, step)
        stepMap.putIfAbsent(dependsOnName, dependsOnStep)
    }

    var time = 0
    while (stepMap.isNotEmpty()) {
        val activeSteps = stepMap.values.filter { it.dependsOn.isEmpty() }.sortedBy { it.step }.take(5)
        val nextFinished = activeSteps.sortedBy { it.remainingTime }.first()
        val timeToFinish = nextFinished.remainingTime
        time += timeToFinish
        activeSteps.onEach { it.remainingTime -= timeToFinish }
        stepMap.remove(nextFinished.step)
        stepMap.values.forEach { it.dependsOn.remove(nextFinished.step) }
    }

    println(time)
}

data class StepWithTime(val step: Char, val dependsOn: MutableSet<Char> = mutableSetOf(), var remainingTime: Int = step - 'A' + 61)