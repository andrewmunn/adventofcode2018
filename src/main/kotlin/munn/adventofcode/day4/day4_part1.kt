package munn.adventofcode.day4

import java.io.File

fun main(args: Array<String>) {
    val regex = Regex("""\[1518\-(\d\d)\-(\d\d) (\d\d):(\d\d)\] (falls asleep|wakes up|Guard #(\d+) begins shift)""")

    val sleeps = mutableListOf<Sleep>()
    var currentGuard: Int? = null
    var sleepTime: Int? = null

    // parse input into list of sleep events
    File(args.first()).readLines().sorted().forEach { line ->
        val values = regex.matchEntire(line)!!.groupValues
        val min = values[4].toInt()
        when {
            values[5].startsWith("Guard") -> currentGuard = values[6].toInt()
            values[5] == "falls asleep" -> sleepTime = min
            else -> sleeps.add(Sleep(currentGuard!!, sleepTime!!, min))
        }
    }

    // find guard that sleeps the most
    val max = sleeps.groupBy { it.id }.entries.maxBy { (_, events) ->
        events.sumBy { it.end - it.start }
    }!!

    // find which minute the guard sleeps the most
    val minutes = IntArray(60)
    max.value.forEach {
        for (i in it.start until it.end) {
            minutes[i]++
        }
    }
    val maxMinVal = minutes.max()

    println(max.key * minutes.indexOfFirst { it == maxMinVal })
}

data class Sleep(val id: Int, val start: Int, val end: Int)