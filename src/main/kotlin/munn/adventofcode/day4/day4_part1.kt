package munn.adventofcode.day4

import munn.adventofcode.day3.Claim
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime

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
        events.sumBy { it.end - it.start }
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

data class Sleep(val id: Int, val start: Int, val end: Int)