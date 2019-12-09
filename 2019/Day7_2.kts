import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue

val instructions = /*File("input/day7.txt")
        .readText()*/
"3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5"
        .split(",")
        .map { it.toInt() }
        .toIntArray()

val numbers = setOf(9, 8, 7, 6, 5)
val input = 0
var max = 0

for (zero in numbers) {
    for (one in numbers.minus(zero)) {
        for (two in numbers.minus(zero).minus(one)) {
            for (three in numbers.minus(zero).minus(one).minus(two)) {
                val four = numbers.minus(zero).minus(one).minus(two).minus(three).first()
                println("$zero $one $two $three $four")

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
                i4.write(four)

                val computers = listOf<IntCodeComputer>(
                        IntCodeComputer("A", i0, i1),
                        IntCodeComputer("B", i1, i2),
                        IntCodeComputer("C", i2, i3),
                        IntCodeComputer("D", i3, i4),
                        IntCodeComputer("E", i4, i0)
                )

                println()
                var index = 0
                do {
                    print('.')
                    val returnCode = computers[index].run(instructions)
                    index = (index + 1) % computers.size
                } while (returnCode != 99)
                println()

                println("$i0 $i1 $i2 $i3 $i4")

                val thrust = i0.read()!!
                if (thrust > max) max = thrust
                println("Max = $max")
            }
        }
    }
}

println(max)

class IntCodeComputer(val label: String, val input: IOCoordinator, val output: IOCoordinator, var opIndex: Int = 0) {
    fun run(instructions: IntArray): Int {
        while (true) {
            val instructionCode = instructions[opIndex].toString()

            val l = instructionCode.length
            val ones = instructionCode.substring(l - 1).toInt()
            val tens = if (l > 1) instructionCode.substring(l - 2, l - 1).toInt() else 0

            val hundreds = if (l > 2) instructionCode.substring(l - 3, l - 2).toInt() else 0
            val thousands = if (l > 3) instructionCode.substring(l - 4, l - 3).toInt() else 0
            val tenthousands = if (l > 4) instructionCode.substring(l - 5, l - 4).toInt() else 0

            val opCode = ones + (tens * 10)
            val modes: List<Mode> = arrayOf(hundreds, thousands, tenthousands).map {
                when (it) {
                    0 -> Mode.POSITION
                    1 -> Mode.IMMEDIATE
                    else -> Exception("Unknown mode")
                }
            }.map { it as Mode }

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
                    if (inValue != null) {
                        instructions[instructions[opIndex + 1]] = inValue
                        opIndex += 2
                    } else {
                        return 3
                    }
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
                    return 99
                }
                else -> throw Exception("Unknown opcode $opCode")
            }
            Thread.sleep(50)
        }
    }
}

enum class Mode {
    POSITION, // 0
    IMMEDIATE // 1
}

class IOCoordinator(val label: Int, val queue: Queue<Int> = LinkedBlockingQueue<Int>()) {
    fun read() : Int? {
        //println("Attempting to read from $label")
        val value = queue.poll()
        //println("$label: reading $value")
        return value
    }

    fun write(value: Int) {
        //println("$label: writing $value")
        queue.add(value)
    }

    override fun toString(): String {
        return "$label: $queue"
    }
}