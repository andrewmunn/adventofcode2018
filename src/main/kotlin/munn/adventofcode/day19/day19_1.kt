package munn.adventofcode.day19

import java.io.File
import kotlin.system.measureTimeMillis

fun main(args: Array<String>) {

    val registers = mutableListOf(0, 0, 0, 0, 0, 0)
    val instructionSet = createInstructionSet(registers)

    val program: List<InstructionWithArgs> = File(args.first()).readLines()
        .drop(1)
        .map { it.split(" ") }
        .map { InstructionWithArgs(instructionSet[it[0]]!!, it[1].toInt(), it[2].toInt(), it[3].toInt()) }

    val ipRegister = 3

    val time = measureTimeMillis {
        var ip = 0
        while (ip < program.size && ip >= 0) {
            registers[ipRegister] = ip
            val instr = program[ip]
            registers[instr.outRegister] = instr.instruction(instr.arg1, instr.arg2)
            ip = ++registers[ipRegister]
        }
    }

    println(registers[0])
    println("it took $time")
}

typealias Instruction = (Int, Int) -> Int

class InstructionWithArgs(val instruction: Instruction, val arg1: Int, val arg2: Int, val outRegister: Int)

private fun createInstructionSet(registers: List<Int>) = mapOf<String, Instruction>(
    "addr" to { arg1, arg2 ->
        registers[arg1] + registers[arg2]
    },
    "addi" to { arg1, arg2 ->
        registers[arg1] + arg2
    },
    "mulr" to { arg1, arg2 ->
        registers[arg1] * registers[arg2]
    },
    "muli" to { arg1, arg2 ->
        registers[arg1] * arg2
    },
    "banr" to { arg1, arg2 ->
        registers[arg1] and registers[arg2]
    },
    "bani" to { arg1, arg2 ->
        registers[arg1] and arg2
    },
    "borr" to { arg1, arg2 ->
        registers[arg1] or registers[arg2]
    },
    "bori" to { arg1, arg2 ->
        registers[arg1] or arg2
    },
    "setr" to { arg1, _ ->
        registers[arg1]
    },
    "seti" to { arg1, _ ->
        arg1
    },
    "gtir" to { arg1, arg2 ->
        if (arg1 > registers[arg2]) 1 else 0
    },
    "gtri" to { arg1, arg2 ->
        if (registers[arg1] > arg2) 1 else 0
    },
    "gtrr" to { arg1, arg2 ->
        if (registers[arg1] > registers[arg2]) 1 else 0
    },
    "eqir" to { arg1, arg2 ->
        if (arg1 == registers[arg2]) 1 else 0
    },
    "eqri" to { arg1, arg2 ->
        if (registers[arg1] == arg2) 1 else 0
    },
    "eqrr" to { arg1, arg2 ->
        if (registers[arg1] == registers[arg2]) 1 else 0
    })