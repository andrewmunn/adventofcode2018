package munn.adventofcode.day16

import munn.adventofcode.utils.buildList
import java.io.File

fun main(args: Array<String>) {
    val registers = mutableListOf(0, 0, 0, 0)
    val instructions = createInstructions(registers)

    val opcodeMap = mutableMapOf<Int, Instruction>()
    val sampleData = File(args.first()).readLines().chunked(4).toMutableList()

    var i = 0
    while (opcodeMap.size < 16) {
        val data = sampleData[i % sampleData.size]
        val before = data[0].substringAfter("[").dropLast(1).split(",").map { it.trim().toInt() }
        val input = data[1].split(" ").map { it.trim().toInt() }
        val after = data[2].substringAfter("[").dropLast(1).split(",").map { it.trim().toInt() }
        val opcodeCount = instructions
            .filter { (_, inst) ->
                registers.clear()
                registers.addAll(before)
                registers[input[3]] = inst(input[1], input[2])
                registers == after
            }
            .map { it.second }
            .filterNot { it in opcodeMap.values }
        if (opcodeCount.size == 1) {
            opcodeMap[input[0]] = opcodeCount.single()
            println("Found opcode ${input[0]}")
        }
        i++
    }

    val codes = File(args[1]).readLines().map { it.split(" ").map { it.trim().toInt() } }

    registers.clear()
    registers.addAll(listOf(0,0,0,0))
    codes.forEach { code ->
        registers[code[3]] = opcodeMap[code[0]]!!.invoke(code[1], code[2])
    }

    println(registers)
}

typealias Instruction = (Int, Int) -> Int

fun createInstructions(registers: MutableList<Int>) = buildList<Pair<String, Instruction>> {
    add("addr" to { arg1, arg2 ->
        registers[arg1] + registers[arg2]
    })
    add("addi" to { arg1, arg2 ->
        registers[arg1] + arg2
    })
    add("mulr" to { arg1, arg2 ->
        registers[arg1] * registers[arg2]
    })
    add("muli" to { arg1, arg2 ->
        registers[arg1] * arg2
    })
    add("banr" to { arg1, arg2 ->
        registers[arg1] and registers[arg2]
    })
    add("bani" to { arg1, arg2 ->
        registers[arg1] and arg2
    })
    add("borr" to { arg1, arg2 ->
        registers[arg1] or registers[arg2]
    })
    add("bori" to { arg1, arg2 ->
        registers[arg1] or arg2
    })
    add("setr" to { arg1, _ ->
        registers[arg1]
    })
    add("seti" to { arg1, _ ->
        arg1
    })
    add("gtir" to { arg1, arg2 ->
        if (arg1 > registers[arg2]) 1 else 0
    })
    add("gtri" to { arg1, arg2 ->
        if (registers[arg1] > arg2) 1 else 0
    })
    add("gtrr" to { arg1, arg2 ->
        if (registers[arg1] > registers[arg2]) 1 else 0
    })
    add("eqir" to { arg1, arg2 ->
        if (arg1 == registers[arg2]) 1 else 0
    })
    add("eqri" to { arg1, arg2 ->
        if (registers[arg1] == arg2) 1 else 0
    })
    add("eqrr" to { arg1, arg2 ->
        if (registers[arg1] == registers[arg2]) 1 else 0
    })
}