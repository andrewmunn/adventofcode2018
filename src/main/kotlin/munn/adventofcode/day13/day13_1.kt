package munn.adventofcode.day13

import munn.adventofcode.day6.Point
import java.io.File

fun main(args: Array<String>) {

    val lines = File(args.first()).readLines()
    val width = lines.maxBy { it.length }!!.length
    val height = lines.size
    val carts = mutableListOf<Cart>()
    val tracks = mutableMapOf<Point, Track?>()

    fun isCharAt(p: Point, vararg chars: Char): Boolean = lines.getOrNull(p.y)?.getOrNull(p.x) ?: ' ' in chars

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val p = Point(x, y)
            tracks[p] = when (c) {
                '-' -> Track(p, TrackType.HORIZONTAL)
                '|' -> Track(p, TrackType.VERTICAL)
                '+' -> Track(p, TrackType.INTERSECTION)
                '\\' -> Track(p, if (isCharAt(p.dX(-1), '-', '+')) TrackType.LEFT_DOWN else TrackType.RIGHT_UP)
                '/' -> Track(p, if(isCharAt(p.dX(-1), '-', '+')) TrackType.LEFT_UP else TrackType.RIGHT_DOWN)
                '<' -> Track(p, TrackType.HORIZONTAL).also { carts.add(Cart(Direction.LEFT, it)) }
                '^' -> Track(p, TrackType.VERTICAL).also { carts.add(Cart(Direction.UP, it)) }
                '>' -> Track(p, TrackType.HORIZONTAL).also { carts.add(Cart(Direction.RIGHT, it)) }
                'v' -> Track(p, TrackType.VERTICAL).also { carts.add(Cart(Direction.DOWN, it)) }
                ' ' -> null
                else -> error("unexpected char $c")
            }
        }
    }

    while (carts.size > 1) {
        carts.sortWith(compareBy({ it.location.location.y}, {it.location.location.x}))
        var i = 0
        while (i < carts.size) {
            val cart = carts[i]
            val nextTrack = tracks[cart.nextLocation()]!!
            cart.direction = when (nextTrack.type) {
                TrackType.HORIZONTAL, TrackType.VERTICAL -> cart.direction
                TrackType.INTERSECTION -> {
                    cart.direction.turn(cart.nextTurn).also {
                        cart.nextTurn = TurnType.values()[(cart.nextTurn.ordinal  + 1) % 3]
                    }
                }
                TrackType.LEFT_UP -> if (cart.direction == Direction.RIGHT) Direction.UP else Direction.LEFT
                TrackType.LEFT_DOWN ->if (cart.direction == Direction.RIGHT) Direction.DOWN else Direction.LEFT
                TrackType.RIGHT_UP -> if (cart.direction == Direction.LEFT) Direction.UP else Direction.RIGHT
                TrackType.RIGHT_DOWN -> if (cart.direction == Direction.LEFT) Direction.DOWN else Direction.RIGHT
            }
            cart.location = nextTrack
            val collidingCarts = carts.filter { it != cart && cart.location.location == it.location.location }
            if (collidingCarts.isNotEmpty()) {
                carts.removeAll(collidingCarts)
                i = carts.indexOf(cart)
                carts.removeAt(i)
            } else {
                i++
            }
        }
    }

    println(carts.single().location)
}

enum class TurnType {
    LEFT, CENTER, RIGHT
}

enum class Direction {
    LEFT, UP, RIGHT, DOWN;

    fun turn(turn: TurnType): Direction {
       return when (turn) {
           TurnType.LEFT -> Direction.values()[(this.ordinal + 3) % 4]
           TurnType.CENTER -> this
           TurnType.RIGHT -> Direction.values()[(this.ordinal + 1) % 4]
       }
    }
}

data class Cart(var direction: Direction, var location: Track, var nextTurn: TurnType = TurnType.LEFT) {

    fun nextLocation(): Point {
        val p = location.location
        return when (direction) {
            Direction.LEFT -> p.dX(-1)
            Direction.UP -> p.dY(-1)
            Direction.RIGHT -> p.dX(1)
            Direction.DOWN -> p.dY(1)
        }
    }
}

enum class TrackType {
    HORIZONTAL,
    VERTICAL,
    INTERSECTION,
    LEFT_UP,
    LEFT_DOWN,
    RIGHT_UP,
    RIGHT_DOWN
}

data class Track(val location: Point, val type: TrackType)