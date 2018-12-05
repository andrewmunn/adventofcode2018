package munn.adventofcode.day5

import munn.adventofcode.utils.mutate
import java.io.File

fun main(args: Array<String>) {
    val input = File(args.first()).readText()
    // val input = "dabAcCaCBAcCcaDA"

    for (polymer in 'a'..'z') {
        var pre = input.filter { it.toLowerCase() != polymer }
        while (true) {
            val post = pre.mutate {
                var i = 0
                while (i < length - 1) {
                    val first = get(i)
                    val second = get(i + 1)
                    if (Math.abs(first - second) == 32) {
                        delete(i, i + 2)
                    }
                    i++
                }
            }

            if (post == pre) {
                break
            }

            pre = post
        }
        println("for polymer $polymer the length is ${pre.length}")
    }
}