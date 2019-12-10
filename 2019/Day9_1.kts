import java.io.File
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.jvm.kotlinProperty

val value = ArrayList<Long> (File("input/day9.txt")
        .readText()
        .split(",")
        .map { it.toLong() }
        .toMutableList() )

val input = longArrayOf(1)

println(runInstructions(value, input))

fun runInstructions(instructions: ArrayList<Long>, input: LongArray) : MutableList<Long> {
    var inputIndex = 0
    val output = mutableListOf<Long>()

    var opIndex = 0
    var relativeBase = 0

    while (true) {
        while (instructions.capacity() < opIndex + 10 || instructions.capacity() < instructions[opIndex.toInt() + 3].toInt()) {
            val newSize = (instructions.capacity() * 1.5).toInt()
            println("Increasing capacity from ${instructions.capacity()} to $newSize...")
            instructions.ensureCapacity(newSize)

            for (i in instructions.size..instructions.capacity()) {
                instructions.add(0L)
            }

            println(instructions.size)
        }

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
            2 -> Mode.RELATIVE
            else -> Exception("Unknown mode")
        }}.map { it as Mode }

        when (opCode) {
            1 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                val offset = if (modes[2] == Mode.RELATIVE) relativeBase else 0
                instructions[offset + instructions[opIndex.toInt() + 3].toInt()] = first + second
                opIndex += 4
            }
            2 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                val offset = if (modes[2] == Mode.RELATIVE) relativeBase else 0
                instructions[offset + instructions[opIndex.toInt() + 3].toInt()] = first * second
                opIndex += 4
            }
            3 -> {
                val inValue = input[inputIndex]
                inputIndex++
                val offset = if (modes[0] == Mode.RELATIVE) relativeBase else 0
                instructions[offset + instructions[opIndex.toInt() + 1].toInt()] = inValue
                opIndex += 2
            }
            4 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                output.add(first)
                opIndex += 2
            }
            5 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                if (first != 0L) {
                    opIndex = second.toInt()
                } else {
                    opIndex += 3
                }
            }
            6 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                if (first == 0L) {
                    opIndex = second.toInt()
                } else {
                    opIndex += 3
                }
            }
            7 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                val thirdIndex = when (modes[2]) {
                    Mode.POSITION -> instructions[opIndex.toInt() + 3]
                    Mode.RELATIVE -> relativeBase + instructions[opIndex.toInt() + 3]
                    Mode.IMMEDIATE -> opIndex.toLong() + 3
                }

                if (first < second) {
                    instructions[thirdIndex.toInt()] = 1
                } else {
                    instructions[thirdIndex.toInt()] = 0
                }
                opIndex += 4
            }
            8 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                val second = getValue(modes, instructions, opIndex, 2, relativeBase)
                val thirdIndex = when (modes[2]) {
                    Mode.POSITION -> instructions[opIndex.toInt() + 3]
                    Mode.RELATIVE -> relativeBase + instructions[opIndex.toInt() + 3]
                    Mode.IMMEDIATE -> opIndex.toLong() + 3
                }
                if (first == second) {
                    instructions[thirdIndex.toInt()] = 1
                } else {
                    instructions[thirdIndex.toInt()] = 0
                }
                opIndex += 4
            }
            9 -> {
                val first = getValue(modes, instructions, opIndex, 1, relativeBase)
                relativeBase += first.toInt()
                opIndex += 2
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
    IMMEDIATE, // 1
    RELATIVE // 2
}

fun getValue(modes: List<Mode>, instructions: MutableList<Long>, opIndex: Int, paramIndex: Int, relativeBase: Int): Long {
    val mode = modes[paramIndex - 1]
    return when (mode) {
        Mode.POSITION -> instructions[instructions[opIndex.toInt() + paramIndex].toInt()]
        Mode.IMMEDIATE -> instructions[opIndex.toInt() + paramIndex]
        Mode.RELATIVE -> instructions[relativeBase + instructions[opIndex.toInt() + paramIndex].toInt()]
    }
}

fun ArrayList<Long>.capacity(): Int {
    val field = ArrayList::class.java.getDeclaredField("elementData")
    field.isAccessible = true

    val elements: Array<Any?> = field.get(this) as Array<Any?>

    return elements.size
}