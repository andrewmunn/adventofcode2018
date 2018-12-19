package munn.adventofcode.day15

import java.io.File
import java.util.*


fun main(args: Array<String>) {

    val input = File(args.first()).readLines()
    // println("Initially:")
    // printBoard(map)
    var elfAttackPower = 3

    fun attack(map: Grid, location: Grid.Location): Boolean {
        val toAttack =
            map.neigborPlayers(location).filter { it.player!!.isElf != location.player!!.isElf }.sortedWith(
                compareBy({ it.player!!.hp }, { it.index })
            ).firstOrNull()
        if (toAttack != null) {
            toAttack.player!!.hp -= if (toAttack.player!!.isElf) 3 else elfAttackPower
            // println("$location attacked $toAttack!")
            if (toAttack.player!!.hp <= 0) {
                toAttack.player = null
            }
            return true
        }
        return false
    }

    while (true) {
        var elfDied = false
        val map = Grid(input)
        elfAttackPower++
        var roundsCompleted = 0
        while (!elfDied) {
            map.grid.filter { it.player != null }.forEachIndexed { index, location ->
                // algoritm is:
                // find player to attack
                // if no player to attack, game is over
                // if player to attack had distance 1, attack
                // move one step closer to player to attack

                if (map.players.any { it.isElf && it.hp <= 0 }) {
                    println("elf died for attack power $elfAttackPower")
                    elfDied = true
                    return@forEachIndexed
                }

                if (location.player == null) {
                    // println("$location is dead!")
                    return@forEachIndexed
                }

                if (attack(map, location)) {
                    return@forEachIndexed
                }

                val target = map.findDistancesToAttackPositions(location) ?: run {
                    println("elves win with attack power $elfAttackPower")
                    println("${map.players.count { it.isElf && it.hp > 0 }} elves remaining!")
                    println("${map.players.count { !it.isElf && it.hp > 0 }} goblins remaining!")
                    println()
                    println("No players left to attack: ${roundsCompleted * map.players.sumBy { Math.max(it.hp, 0) }}")
                    return
                }

                val player = location.player!!
                // move if we're not already in the right place
                var ancestor = target
                while (ancestor.value > 1) {
                    ancestor = ancestor.ancestor!!
                }
                if (ancestor.value == 1) {
                    check(ancestor.ancestor!!.location == location)
                    // println("$location moved to ${ancestor.location}!")
                    ancestor.location.player = location.player!!
                    location.player = null
                }
                check(ancestor.location.player == player)
                attack(map, ancestor.location)
            }
            roundsCompleted++
            println("after $roundsCompleted rounds:")
            // printBoard(map)
        }
    }
}
fun printBoard(map: Grid) {
    val healthToPrint = mutableListOf<Player>()
    map.grid.forEachIndexed { i, l ->
        print(when {
            l.isRock -> '#'
            l.player == null || l.player!!.hp <= 0 -> '.'
            l.player!!.isElf -> {
                healthToPrint.add(l.player!!)
                'E'
            }
            else -> {
                healthToPrint.add(l.player!!)
                'G'
            }
        })
        if (i % Grid.GRID_DIM == Grid.GRID_DIM - 1) {
            println(healthToPrint.joinToString(separator = ", ", prefix = "   ") { "${if (it.isElf) "E" else "G"}(${it.hp})" })
            healthToPrint.clear()
        }
    }
    println()
}

fun printRanking(map: Grid, distances: Map<Grid.Location, Grid.Distance>, location: Grid.Location) {
    map.grid.forEachIndexed { i, l ->
        print(when {
            l.isRock -> '#'
            l.isFreeSpace() || l == location -> distances[l]!!.value.let { if (it == Int.MAX_VALUE) "x" else it.toString() }
            l.player!!.isElf -> 'E'
            else -> 'G'
        })
        if (i % Grid.GRID_DIM == Grid.GRID_DIM - 1) {
           println()
        }
    }
    println()
}

data class Player(val isElf: Boolean, var hp: Int = 200)

class Grid(lines: List<String>) {

    var players = mutableListOf<Player>()
    var grid = Array(GRID_DIM * GRID_DIM) { i ->
        val x = i % GRID_DIM
        val y = i / GRID_DIM
        when (val c = lines[y][x]) {
            '#' -> Location(i, isRock = true)
            '.' -> Location(i, isRock = false)
            'E' -> Location(i, isRock = false, player = Player(isElf = true)).also { players.add(it.player!!) }
            'G' -> Location(i, isRock = false, player = Player(isElf = false)).also { players.add(it.player!!) }
            else -> error("Unexpected input $c")
        }
    }

    fun findDistancesToAttackPositions(location: Location): Distance? {
        val player = location.player!!
        val targets = grid.filter { it.player != null && it.player!!.isElf != player.isElf && it.player!!.hp > 0 }.toSet()
        if (targets.isEmpty()) {
            return null
        }

        val queue = PriorityQueue<Distance>()
        val distances = grid.associateTo(mutableMapOf()) { it to Distance(it, Int.MAX_VALUE, null) }
        val nodesVisited = mutableSetOf<Location>()
        distances[location] = Distance(location, 0, null)
        grid.forEach { queue.add(distances[it]!!) }
        while (queue.isNotEmpty()) {
            val current = queue.remove()
            if (current.location != location && !current.location.isFreeSpace()) {
                // we've ranked everything we can rank
                break
            }
            val neighbors = freeNeighbors(current.location)
            for (neighbor in neighbors) {
                val existingDistance = distances[neighbor]!!
                val newDistance = Distance(neighbor, current.value + 1, current)
                if (newDistance < existingDistance) {
                    distances[neighbor] = newDistance
                    if (neighbor !in nodesVisited) {
                        check(queue.remove(existingDistance))
                        queue.add(newDistance)
                    }
                }
            }
            nodesVisited.add(current.location)
        }
        //printRanking(this, distances, location)
        val freeTargets = targets.flatMapTo(mutableSetOf()) { freeNeighbors(it) }
        return freeTargets.sortedWith(compareBy({ distances[it]!!.value }, { it.index }))
            .map { distances[it]!! }
            .firstOrNull { it.value != Int.MAX_VALUE }
            ?: distances[location]
    }

    fun freeNeighbors(location: Location): List<Location> = neighbors(location).filter { it.isFreeSpace() }

    fun neigborPlayers(location: Location): List<Location> = neighbors(location).filter { it.player != null && it.player!!.hp > 0 }

    fun neighbors(location: Location): List<Location> {
        val top = at(location.x, location.y - 1)
        val left = at(location.x - 1, location.y)
        val right = at(location.x + 1, location.y)
        val bottom = at(location.x, location.y + 1)
        return listOfNotNull(top, left, right, bottom)
    }

    fun at(x: Int, y: Int): Location? = grid.getOrNull(y * GRID_DIM + x)

    data class Location(
        val index: Int,
        val isRock: Boolean,
        var player: Player? = null,
        val x: Int = index % GRID_DIM,
        val y: Int = index / GRID_DIM) {

        fun isFreeSpace(): Boolean = !isRock && (player == null || player!!.hp <= 0)
    }

    companion object {
        val GRID_DIM = 32
    }

    class Distance(
        val location: Location,
        val value: Int,
        val ancestor: Distance?) : Comparable<Distance> {

        override fun compareTo(other: Distance): Int {
            return if (value - other.value == 0) {
                location.index - other.location.index
            } else {
                value - other.value
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Distance

            if (location != other.location) return false

            return true
        }

        override fun hashCode(): Int {
            return location.hashCode()
        }
    }
}