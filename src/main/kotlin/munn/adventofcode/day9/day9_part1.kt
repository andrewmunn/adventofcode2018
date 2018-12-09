package munn.adventofcode.day9

fun main() {
    val playerCount = 405
    val lastMarble = 7170000
    var node = Node(0)
    val players = MutableList(playerCount) { 0L }
    var index = 0
   // prLongln("0: $circle")
    (1..lastMarble).forEach { value: Int ->
        val playerIndex = (value - 1) % playerCount
        if (value % 23 != 0) {
            node = node.forwardBy(1)
            node = node.addNext(value.toLong())
        } else {
            node = node.backBy(7)
            players[playerIndex] += (value + node.value)
            node = node.remove()!!
        }
       // prLongln("${playerIndex + 1}: ${node.prLong()}")
    }

    println(players.max())
}

private class Node(var value: Long, next: Node? = null, prev: Node? = null) {

    var next: Node
    var prev: Node
    init {
        this.next = next ?: this
        this.prev = prev ?: this
    }

    fun forwardBy(i: Int): Node {
        var node = this
        repeat(i) { node = node.next }
        return node
    }

    fun backBy(i: Int): Node {
        var node = this
        repeat(i) { node = node.prev }
        return node
    }

    fun addNext(value: Long): Node {
        val node = Node(value, next, this)
        next.prev = node
        next = node
        return node
    }

    fun remove(): Node? {
        prev.next = next
        next.prev = prev
        return next
    }

    fun prLong(): String {
        var toPrLong = this
        return buildString {
           do {
               append("${toPrLong.value} ")
               toPrLong = toPrLong.next
           } while (toPrLong != this@Node)
       }
    }
}