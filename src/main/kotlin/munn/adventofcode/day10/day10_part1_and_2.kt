package munn.adventofcode.day10

import java.io.File
import kotlin.math.min

fun main(args: Array<String>) {

    val regex = Regex("""position=<(-? ?\d+), (-? ?\d+)> velocity=<(-? ?\d+), (-? ?\d+)>""")

    val points = File(args.first()).readLines().map { line ->
        val values = regex.matchEntire(line)!!.groupValues.drop(1).map { it.trim().toInt() }
        PointInfo(values[0], values[1], values[2], values[3])
    }

    var minute = 0
    var xDeltaLast = Int.MAX_VALUE
    var yDeltaLast = Int.MAX_VALUE
    while (true) {
        val testPoints = points.map { it.evolve(minute) }
        val xDelta = testPoints.maxBy { it.x }!!.x - testPoints.minBy { it.x }!!.x
        val yDelta = testPoints.maxBy { it.y }!!.y - testPoints.minBy { it.y }!!.y
       // println("min: $minute xDelta: $xDelta, yDelta: $yDelta")
        if (xDeltaLast < xDelta && yDeltaLast < yDelta) {
            println("Very probable solution at min ${minute - 1}")
            println("xDelta: $xDeltaLast, yDelta: $yDeltaLast")
            minute--
            break
        }
        xDeltaLast = xDelta
        yDeltaLast = yDelta
        minute++
    }

    val solutionPoints = points.map { it.evolve(minute) }
    val xMin = solutionPoints.minBy { it.x }!!.x
    val xMax = solutionPoints.maxBy { it.x }!!.x
    val yMin = solutionPoints.minBy { it.y }!!.y
    val yMax = solutionPoints.maxBy { it.y }!!.y
    (yMin..yMax).forEach { y ->
        (xMin..xMax).forEach { x ->
            if (solutionPoints.any { it.x == x && it.y == y }) {
                print("#")
            } else {
                print(".")
            }
        }
        println()
    }

}

data class PointInfo(val x: Int, val y: Int, val dx: Int, val dy: Int) {

    fun evolve(minutes: Int): PointInfo = copy(x = x + dx * minutes, y = y + dy * minutes)
}
