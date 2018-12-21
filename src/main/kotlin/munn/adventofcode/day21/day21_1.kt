package munn.adventofcode.day21

fun main() {
    val reg0 = 0
    var reg1 = 0
    var reg2 = 0
    var reg5 = 0

    var reg4 = 0
    reg1 = reg4 or 65536
    reg4 = 678134
    while (true) {
        reg5 = reg1 and 255
        reg4 += reg5
        reg4 = reg4 and 16777215
        reg4 *= 65899
        reg4 = reg4 and 16777215
        if (256 > reg1) {
            println(reg4)
            break
        } else {
            reg5 = 0
            while (true) {
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