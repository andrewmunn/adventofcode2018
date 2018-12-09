package munn.adventofcode.day9

fun main() {
    val playerCount = 405
    val lastMarble = 71700
    val players = MutableList(playerCount) { 0 }
    val circle = mutableListOf(0)
    var index = 0
    (1..lastMarble).forEach { value ->
        val playerIndex = (value - 1) % playerCount
        if (value % 23 != 0) {
            index = (index + 2) % (circle.size)
            circle.add(index + 1, value)
        } else {
            index -= 6
            if (index < 0) {
                index += circle.size
            }
            players[playerIndex] += (value + circle[index])
            circle.removeAt(index)
            index--
        }

       // println("${playerIndex + 1}: $circle")
    }

    println(players.max())
}
