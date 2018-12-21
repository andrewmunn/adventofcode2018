package munn.adventofcode.day21

fun main() {
    val reg0 = 0
    var reg1 = 0
    var reg2 = 0
    var reg5 = 0

    var reg4 = 0
    reg1 = reg4 or 65536
    reg4 = 678134
    var outerLoopCount = 0
    var innerLoopCount = 0
    val seenIntegers = mutableSetOf<Int>()
    while (true) {
        outerLoopCount++
        reg5 = reg1 and 255
        reg4 += reg5
        reg4 = reg4 and 16777215
        reg4 *= 65899
        reg4 = reg4 and 16777215
        if (256 > reg1) {
            if (!seenIntegers.add(reg4)) {
                println("found dupe at $reg4. Previous value was ${seenIntegers.last()}")
                break
            }
            reg1 = reg4 or 65536
            reg4 = 678134

        } else {
            reg5 = 0
            while (true) {
                innerLoopCount++
                reg2 = reg5 + 1
                reg2 *= 256
                if (reg2 > reg1) {
                    reg1 = reg5
                    break
                } else {
                    reg5 += 1
                }
            }
        }
    }
    println(reg1)
}