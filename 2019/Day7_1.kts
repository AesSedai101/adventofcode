import java.io.File
import java.lang.Exception
import java.util.*

val instructions = File("input/day7.txt")
        .readText()
        .split(",")
        .map { it.toInt() }
        .toIntArray()


val numbers = setOf(0, 1, 2, 3, 4)
val input = 0
var max = 0

for (zero in numbers) {
    for (one in numbers.minus(zero)) {
        for (two in numbers.minus(zero).minus(one)) {
            for (three in numbers.minus(zero).minus(one).minus(two)) {
                val i0 = IOCoordinator(0)
                i0.write(zero)
                i0.write(input)

                val i1 = IOCoordinator(1)
                i1.write(one)

                val i2 = IOCoordinator(2)
                i2.write(two)

                val i3 = IOCoordinator(3)
                i3.write(three)

                val i4 = IOCoordinator(4)
                val four = numbers.minus(zero).minus(one).minus(two).minus(three).first()
                i4.write(four)

                val output = IOCoordinator(5)

                //println("$zero $one $two $three $four")

                runIntCodes(instructions, i0, i1)
                runIntCodes(instructions, i1, i2)
                runIntCodes(instructions, i2, i3)
                runIntCodes(instructions, i3, i4)
                runIntCodes(instructions, i4, output)

                val thrust = output.read()
                if (thrust > max) max = thrust
                //println("Max = $max")
            }
        }
    }
}

println(max)

fun runIntCodes(instructions: IntArray, input: IOCoordinator, output: IOCoordinator) : IOCoordinator {
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
                val inValue = input.read()
                instructions[instructions[opIndex + 1]] = inValue
                opIndex += 2
            }
            4 -> {
                val first = if (modes[0] == Mode.POSITION) instructions[instructions[opIndex + 1]] else instructions[opIndex + 1]
                output.write(first)
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

class IOCoordinator(val label: Int, val queue: Queue<Int> = LinkedList<Int>()) {
    fun read() : Int {
        val value = queue.poll()
        return value
    }

    fun write(value: Int) {
        queue.add(value)
    }

    override fun toString(): String {
        return "$label: $queue"
    }
}