import java.io.File

val target = 19690720

var noun = -1
for (n in 0..99) {
    val out = calculateOutput(n, 0)
    if (out > target) {
        noun = n - 1
        break
    }
}

var verb = 0

for (v in 0..99) {
    val out = calculateOutput(noun, v)
    if (out == target) {
        verb = v
        break
    }
}

println("$noun, $verb = ${calculateOutput(noun, verb)} [${100 * noun + verb}] ")

fun calculateOutput(noun: Int, verb: Int): Int {
    val value = File("input/day2.txt").readText()
            .split(",")
            .toMutableList()
            .map { it.toInt() }
            .toIntArray()

    value[1] = noun
    value[2] = verb

    for (i in 0..value.size - 1 step 4) {
        when (value[i]) {
            1 -> {
                value[value[i + 3]] = value[value[i + 1]] + value[value[i + 2]]
            }
            2 -> {
                value[value[i + 3]] = value[value[i + 1]] * value[value[i + 2]]
            }
            99 -> {
                return value[0]
            }
        }
    }

    throw RuntimeException("Oh noes")
}