package munn.adventofcode.day24

import munn.adventofcode.utils.toPair
import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {
    val regex = Regex("""^(\d+) units each with (\d+) hit points ?(\(.+\))? with an attack that does (\d+) (\w+) damage at initiative (\d+)""")

    var isInfection = false
    val armies = File(args.first()).readLines().mapNotNull { line ->
        when {
            line == "Immune System:" -> {
                isInfection = false
                null
            }
            line == "Infection:" -> {
                isInfection = true
                null
            }
            regex.matches(line) -> {
                val values = regex.matchEntire(line)!!.groupValues
                val weakTo = mutableSetOf<String>()
                val immuneTo = mutableSetOf<String>()
                values[3].drop(1).dropLast(1).split("; ").forEach {
                    val conditions = it
                        .removePrefix("weak ")
                        .removePrefix("immune ")
                        .removePrefix("to ")
                        .split(", ")
                        .filter(String::isNotBlank)
                    if (it.startsWith("weak")) {
                        weakTo.addAll(conditions)
                    } else {
                        immuneTo.addAll(conditions)
                    }
                }
                ArmyGroup(
                    isInfection = isInfection,
                    units = values[1].toInt(),
                    hp = values[2].toInt(),
                    weakTo = weakTo,
                    immuneTo = immuneTo,
                    attack = values[4].toInt(),
                    attackType = values[5],
                    initiative = values[6].toInt())
            }
            else -> null
        }
    }
        .partition { !it.isInfection }

    var low = 84
    val high = 95
    while (low <= high) {
        val armiesToFight = armies.toList().map { it.map { group -> group.copy(low) } }.toPair()
        println("testing $low")
        val winner = fight(armiesToFight)
        if (winner.first().isInfection) {
            low++
        } else {
            println(winner.sumBy { it.units })
        }
    }
}

tailrec fun fight(armies: Pair<List<ArmyGroup>, List<ArmyGroup>>): List<ArmyGroup> {
    armies.toList().singleOrNull { it.isNotEmpty() }?.let { victor ->
        return victor
    }

    selectTargets(armies.first, armies.second.toMutableSet())
    selectTargets(armies.second, armies.first.toMutableSet())

    val attackingOrder = armies.toList().flatten().sortedByDescending { it.initiative }
    attackingOrder.forEach { attacker ->
        if (attacker.units <= 0) {
            return@forEach
        }
        attacker.target?.let { target ->
            target.units -= attacker.damageTo(target) / target.hp
        }
    }

    return fight(armies.toList().map { it.filter { group -> group.units > 0 } }.toPair())
}

fun selectTargets(attackers: List<ArmyGroup>, targets: MutableSet<ArmyGroup>) {
    attackers.sortedWith(compareBy( { -it.effectivePower() }, { -it.initiative } )).forEach { attacker ->
        attacker.target = targets.filter { attacker.damageTo(it) > 0 }
            .sortedWith(compareBy( { -attacker.damageTo(it) }, { -it.effectivePower() },  { -it.initiative } ))
            .firstOrNull()
        attacker.target?.let { targets.remove(it) }
    }
}

class ArmyGroup(
    val isInfection: Boolean,
    var units: Int,
    val hp: Int,
    val weakTo: Set<String>,
    val immuneTo: Set<String>,
    private val attack: Int,
    val attackType: String,
    val initiative: Int,
    var target: ArmyGroup? = null,
    val boost: Int = 0) {

    fun copy(boost: Int): ArmyGroup {
        return ArmyGroup(
            isInfection = isInfection,
            units = units,
            hp = hp,
            weakTo = weakTo,
            immuneTo = immuneTo,
            attack = attack,
            attackType = attackType,
            initiative = initiative,
            target = target,
            boost = if (!isInfection) boost else 0)
    }

    fun effectivePower() = units * (attack + boost)

    fun damageTo(target: ArmyGroup): Int {
        return when (attackType) {
            in target.immuneTo -> 0
            in target.weakTo -> effectivePower() * 2
            else -> effectivePower()
        }
    }
}