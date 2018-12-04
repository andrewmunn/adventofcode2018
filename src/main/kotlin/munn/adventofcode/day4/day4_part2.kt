package munn.adventofcode.day4

import java.io.File

fun main(args: Array<String>) {
    val regex = Regex("""\[1518\-(\d\d)\-(\d\d) (\d\d):(\d\d)\] (falls asleep|wakes up|Guard #(\d+) begins shift)""")

    val sleeps = mutableListOf<Sleep>()
    var currentGuard: Int? = null
    var sleepTime: Int? = null
    File(args.first()).readLines().sorted().forEach { line ->
        val values = regex.matchEntire(line)!!.groupValues
        val min = values[4].toInt()
        when {
            values[5].startsWith("Guard") -> currentGuard = values[6].toInt()
            values[5] == "falls asleep" -> sleepTime = min
            else -> sleeps.add(Sleep(currentGuard!!, sleepTime!!, min))
        }
    }

    sleeps.forEach {
        println(it)
    }

    val max = sleeps.groupBy { it.id }.entries.maxBy { (_, events) ->
        val minutes = IntArray(60)
        events.forEach {
            for (i in it.start until it.end) {
                println(i)
                minutes[i]++
            }
        }
        minutes.max()!!
    }!!

    val minutes = IntArray(60)
    max.value.forEach {
        for (i in it.start until it.end) {
            println(i)
            minutes[i]++
        }
    }

    val maxMinVal = minutes.max()

    println(max.key * minutes.indexOfFirst { it == maxMinVal })

}