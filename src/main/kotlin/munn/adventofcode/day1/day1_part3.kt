package munn.adventofcode.day1

import java.io.File

fun main(args: Array<String>) {
    val seen = mutableSetOf<Int>()
    val nums =  File(args.first()).readLines().map { it.toInt() }
    var index = 0
    var foundDupe = false
    var sum = 0
    while (true) {
        if (!seen.add(sum)) {
            println("Found dupe: $sum after $index iterations")
            break
        }
        sum += nums[index++ % nums.size]
    }
}