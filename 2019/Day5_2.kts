import java.io.File
import java.lang.Exception

val value = File("input/day5.txt")
        .readText()
        .split(",")
        .map { it.toInt() }
        .toIntArray()

val input = intArrayOf(5)

println(runInstructions(value, input))

fun runInstructions(instructions: IntArray, input: IntArray) : MutableList<Int> {
    var inputIndex = 0
    val output = mutableListOf<Int>()

    var opIndex = 0

    while (true) {
        val instructionCode = instructions[opIndex].toString()

        val l = instructionCode.length
        val ones = instructionCode.substring(l-1).toInt()
        val tens = if (l > 1) instructionCode.substring(l-2, l-1).toInt() else 0

        val hundreds = if (l > 2) instructionCode.substring(l-3, l-2).toInt() else 0
        val thousands = if (l > 3) instructionCode.substring(l-4, l-3).toInt() else 0
        val tenthousands = if (l > 4) instructionCode.substring(l-5, l-4).toInt() else 0

        val opCode = ones + (tens * 10)
        val modes: List<Mode> = arrayOf(hundreds, thousands, tenthousands).map { when(it) {
            0 -> Mode.POSITION
            1 -> Mode.IMMEDIATE
            else -> Exception("Unknown mode")
        }}.map { it as Mode }

        when (opCode) {
            1 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                instructions[instructions[opIndex + 3]] = first + second
                opIndex += 4
            }
            2 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                instructions[instructions[opIndex + 3]] = first * second
                opIndex += 4
            }
            3 -> {
                val inValue = input[inputIndex]
                inputIndex++
                instructions[instructions[opIndex + 1]] = inValue
                opIndex += 2
            }
            4 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                output.add(first)
                opIndex += 2
            }
            5 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                if (first != 0) {
                    opIndex = second
                } else {
                    opIndex += 3
                }
            }
            6 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                if (first == 0) {
                    opIndex = second
                } else {
                    opIndex += 3
                }
            }
            7 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                val thirdIndex = if (modes[2] == Mode.POSITION) instructions[opIndex + 3] else (opIndex + 3)
                if (first < second) {
                    instructions[thirdIndex] = 1
                } else {
                    instructions[thirdIndex] = 0
                }
                opIndex += 4
            }
            8 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                val second = if (modes[1] == Mode.POSITION) instructions[instructions[opIndex + 2]] else instructions[opIndex + 2]
                val thirdIndex = if (modes[2] == Mode.POSITION) instructions[opIndex + 3] else (opIndex + 3)
                if (first == second) {
                    instructions[thirdIndex] = 1
                } else {
                    instructions[thirdIndex] = 0
                }
                opIndex += 4
            }
            99 -> {
                return output
            }
            else -> throw Exception("Unknown opcode $opCode")
        }
    }
}

enum class Mode {
    POSITION, // 0
    IMMEDIATE // 1
}