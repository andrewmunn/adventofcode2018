package munn.adventofcode.day19

fun main() {
    val flag = 1
    var reg3 = 17
    var reg5 = 0

    // here's a function we jump to at the beginnging
    var reg2 = 2
    reg3++
    reg2 *= reg2
    reg3++
    reg2 *= 19
    reg3++
    reg2 *= 11
    reg3++
    reg5 += 4
    reg3++
    reg5 *= 22
    reg3++
    reg5 += 16
    reg3++
    reg2 += reg5 // instr 24
    reg3++

    reg3 = reg3 + flag
    reg3++
    if (flag == 1) {
        reg5 = reg3
        reg3++
        reg5 *= reg3
        reg3++
        reg5 += reg3
        reg3++
        reg5 *= reg3
        reg3++
        reg5 *= 14
        reg3++
        reg5 *= reg3
        reg2 += reg5
    }

    println(reg5)
    println(reg2)

    // back to instruct
    // ion 1
    var out = 0
    var a = 1
    while (a <= reg2) {
        var b = 1 // line 3
      /* while (b <= reg2) {
            if (a * b == reg2) {
                out += a
            }
            b += 1
        } */
        if (reg2 % a == 0) {
            out += a
        }
        a++
    }

    println(out)
}