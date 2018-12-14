package munn.adventofcode.day14

fun main(args: Array<String>) {

    val recipesToMake = 170641
    val recipes = mutableListOf(3, 7)
    var elf1 = 0
    var elf2 = 1
    while (recipes.size < recipesToMake + 10) {
        val newRecipies = recipes[elf1] + recipes[elf2]
        recipes.addAll(newRecipies.toString().map { it.toString().toInt() })
        elf1 = (elf1 + recipes[elf1] + 1) % recipes.size
        elf2 = (elf2 + recipes[elf2] + 1) % recipes.size
    }

    println(recipes.subList(recipesToMake, recipesToMake + 10).joinToString("") { it.toString() })
}