package munn.adventofcode.day14

private val i = 1

fun main(args: Array<String>) {

    val recipesToMake = "170641"
    val recipes = mutableListOf(3, 7)
    var elf1 = 0
    var elf2 = 1
    while (true) {
        val newRecipies = recipes[elf1] + recipes[elf2]
        recipes.addAll(newRecipies.toString().map { it.toString().toInt() })
        //println(recipes)
        elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
        elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
        if (recipes.size > 8) {
            val last6 = recipes.subList(recipes.size - 6, recipes.size).joinToString("")
            // println(last6)
            if (last6 == recipesToMake) {
                println(recipes.size - 6)
                break
            }
            val last7 = recipes.subList(recipes.size - 7, recipes.size - 1).joinToString("")
            // println(last7)
            if (last7 == recipesToMake) {
                println(recipes.size - 7)
                break
            }
        }
    }
}