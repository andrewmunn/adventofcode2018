package munn.adventofcode.day22

import munn.adventofcode.utils.Point
import java.util.*

fun main() {
    val depth = 7863 // 510
    val target = Point(14, 760) // Point(10, 10)
    val cave = mutableMapOf<Point, List<Region>>()

    fun regionAt(p: Point): List<Region> {
        return cave.getOrPut(p) {
            val geologicIndex = when {
                p == target -> 0
                p.y == 0 -> p.x * 16807
                p.x == 0 -> p.y * 48271
                else -> regionAt(p.dX(-1)).first().erosionLevel * regionAt(p.dY(-1)).first().erosionLevel
            }
            val erosionLevel = (geologicIndex + depth) % 20183
            val type = Type.values()[erosionLevel % 3]
            if (p.isOrigin() || p == target) {
                listOf(Region(erosionLevel, Tool.TORCH, type, p))
            } else {
                type.tools.map { Region(erosionLevel, it, type, p) }
            }
        }
    }

    fun getNeighbors(region: Region): List<Pair<Region, Int>> {
        val left = region.point.dX(-1).takeIf { it.x >= 0 }
        val top = region.point.dY(-1).takeIf { it.y >= 0 }
        val right = region.point.dX(1).takeIf { it.x <= target.x * 4 } // adjust bounds up if answer not found
        val bottom = region.point.dY(1).takeIf { it.y <= target.y * 1.5 } // adjust bounds up if answer not found
        return listOfNotNull(left, top, right, bottom)
            .flatMap { regionAt(it) }
            .filter { other -> other.tool in region.type.tools || other.point == target }
            .map { other ->
            other to (if (other.tool == region.tool) 1 else 8)
        }
    }

    val originRegion = regionAt(Point.ORIGIN).single()
    val targetRegion = regionAt(target).single() // build the graph
    val queue = PriorityQueue<RegionTime>()
    val times = mutableMapOf(originRegion to RegionTime(originRegion, 0))
    val regionsVisited = mutableSetOf<Region>()
    times[originRegion] = RegionTime(originRegion, 0)
    queue.add(times[originRegion])
    while (queue.isNotEmpty()) {
        val current: RegionTime = queue.remove()
        val neighbors = getNeighbors(current.region).filter { (neighbor, _) -> neighbor !in regionsVisited }
        for ((neighbor, time) in neighbors) {
            val existingMinutes = times[neighbor] ?: RegionTime(neighbor, Int.MAX_VALUE)
            val newMinutes = RegionTime(neighbor, current.minutes + time)
            if (newMinutes < existingMinutes) {
                times[neighbor] = newMinutes
                queue.remove(existingMinutes)
                queue.add(newMinutes)
            }
        }
        regionsVisited.add(current.region)
    }

    println(times[originRegion])
    println(times[targetRegion])
}

private enum class Tool { TORCH, CLIMBING_GEAR, NEITHER }

private enum class Type(val tools: Set<Tool>) {
    ROCKY(setOf(Tool.TORCH, Tool.CLIMBING_GEAR)),
    WET(setOf(Tool.CLIMBING_GEAR, Tool.NEITHER)),
    NARROW(setOf(Tool.TORCH, Tool.NEITHER));
}

private data class Region(
    val erosionLevel: Int,
    val tool: Tool,
    val type: Type,
    val point: Point)

private data class RegionTime(
    val region: Region,
    val minutes: Int) : Comparable<RegionTime> {

    override fun compareTo(other: RegionTime): Int {
        return minutes - other.minutes
    }
}