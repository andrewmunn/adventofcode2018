package munn.adventofcode.day9

import munn.adventofcode.utils.rotate
import java.util.*

// this version takes really long time because the rotate method sucks
fun main() {
    val playerCount = 405
    val lastMarble = 71700
    val circle = LinkedList<Long>()
    circle.add(0L)
    val players = MutableList(playerCount) { 0L }
    (1..lastMarble).forEach { value: Int ->
        val playerIndex = (value - 1) % playerCount
        if (value % 23 != 0) {
            circle.rotate(-2)
            circle.addFirst(value.toLong())
        } else {
            circle.rotate(7)
            players[playerIndex] += (value + circle.removeFirst())
        }
    }

    println(players.max())
}