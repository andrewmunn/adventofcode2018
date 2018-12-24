package munn.adventofcode.day23

import java.awt.Point
import java.io.File

fun main(args: Array<String>) {
    val regex = Regex("""pos=<(-?\d+),(-?\d+),(-?\d+)>, r=(\d+)""")
    val bots = File(args.first()).readLines().map { line ->
        val values = regex.matchEntire(line)!!.groupValues.drop(1).map { it.toInt() }
        Bot(Point3D(values[0], values[1], values[2]), values[3])
    }

    val strongest = bots.maxBy { it.r }!!

    val numInRange = bots.count { bot ->
        strongest.inRange(bot.point)
    }

    println(numInRange)

    val botPoints = bots.map { it.point }
    val minX = botPoints.minBy { it.x }!!.x
    val maxX = botPoints.maxBy { it.x }!!.x
    val minY = botPoints.minBy { it.y }!!.y
    val maxY = botPoints.maxBy { it.y }!!.y
    val minZ = botPoints.minBy { it.z }!!.z
    val maxZ = botPoints.maxBy { it.z }!!.z

    val box = Box(topLeftFront = Point3D(minX, minY, minZ), bottomRightBack = Point3D(maxX, maxY, maxZ))

    val answer = findWinningPoint(bots, box)
    val count = bots.count { it.inRange(answer) }
    println("=======")
    println(answer)
    println(count)
    println(answer.distanceFromOrigin())

}

tailrec fun findWinningPoint(bots: List<Bot>, box: Box): Point3D {
    if (box.isPoint()) {
        return box.bottomRightBack
    }
    val winningSection = box.sections().maxBy {
        findInRangeAtCenterOf(bots, it)
    }!!

    return findWinningPoint(bots, winningSection)
}

fun findInRangeAtCenterOf(bots: List<Bot>, box: Box): Int {
    val center = box.center()
    return bots.count { it.inRange(center) }.also {
        if (it == 778) {
            println(box)
        }
    }
}

data class Box(val topLeftFront: Point3D, val bottomRightBack: Point3D) {

    fun center() = topLeftFront.center(bottomRightBack)

    fun isPoint() = topLeftFront == bottomRightBack

    fun sections(): List<Box> {
        val center = center()
        return listOf(
            Box(topLeftFront, center),
            Box(topLeftFront.copy(x = center.x), center.copy(x = bottomRightBack.x)),
            Box(topLeftFront.copy(y = center.y), center.copy(y = bottomRightBack.y)),
            Box(center.copy(z = topLeftFront.z), bottomRightBack.copy(z = center.z)),
            Box(topLeftFront.copy(z = center.z), center.copy(z = bottomRightBack.z)),
            Box(topLeftFront.copy(x = center.x, z = center.z), bottomRightBack.copy(y = center.y)),
            Box(topLeftFront.copy(y = center.y, z = center.z), bottomRightBack.copy(x = center.x)),
            Box(center, bottomRightBack)
        )
    }
}

data class Bot(val point: Point3D, val r: Int) {

    fun inRange(other: Point3D) = r >= Math.abs(point.x - other.x) + Math.abs(point.y - other.y) + Math.abs(point.z - other.z)
}

data class Point3D(val x: Int, val y: Int, val z: Int) {

    fun center(other: Point3D) = (this + other) / 2

    fun distanceFromOrigin() = Math.abs(x) + Math.abs(y) + Math.abs(z)

    operator fun plus(other: Point3D) = Point3D(x + other.x, y + other.y, z + other.z)

    operator fun div(divisor: Int) =  Point3D(x / divisor, y / divisor, z / divisor)
}