package munn.adventofcode.day17

import munn.adventofcode.utils.Point
import java.io.File

fun main(args: Array<String>) {
    val regex = Regex("""(x|y)=(\d+), (x|y)=(\d+)..(\d+)""")

    val clays = File(args.first()).readLines().flatMap { line ->
        val parts = regex.matchEntire(line)!!.groupValues
        val singleValue = parts[2].toInt()
        val range = parts[4].toInt()..parts[5].toInt()
        range.map {
            if (parts[1] == "x") {
                Point(x = singleValue, y = it)
            } else {
                Point(x = it, y = singleValue)
            }
        }
    }

    val lowestY = clays.minBy { it.y }?.y ?: 0
    val highestY = clays.maxBy { it.y }?.y ?: 0

    val sprig = Point(500, 0)

    val groundMap = clays.associateWithTo(mutableMapOf(sprig to '+')) { '#' }

    fun Map<Point, Char>.at(p: Point) = getOrElse(p) { '.' }

    fun printMap() {
        val minX = groundMap.keys.minBy { it.x }!!.x
        val maxX = groundMap.keys.maxBy { it.x }!!.x
        (0..highestY).forEach { y->
            (minX..maxX).forEach { x->
                print(groundMap.at(Point(x, y)))
            }
            println()
        }
    }

    printMap()

    val impassable = setOf('#', '~')
    fun flow(point: Point, wasBlocked: Boolean): Boolean {
        if (point.y > highestY) {
            return true
        }

        if (groundMap.at(point) == '|' && !wasBlocked) {
            return true
        }

        if (groundMap.at(point) in impassable) {
            return false
        }

        // can we flow down?
        groundMap[point] = '|'
        val down = flow(point.dY(1), false)
        if (down) {
            return true
        }

        // can't flow down, go left and right
        val leftPoint = point.dX(-1)
        val rightPoint = point.dX(1)
        val left = if (!wasBlocked) flow(leftPoint, false) else false
        val right = flow(rightPoint, !left)
        if (!left && !right) {
            // can't flow left or right
            groundMap[point] = '~'
            return false
        }
        return true
    }

    check(flow(sprig.dY(1), false))
    val waterCount = groundMap.entries.count { (p, v) ->
        p.y >= lowestY && p.y <= highestY && (v == '~')
    }

    printMap()
    println(waterCount)
}