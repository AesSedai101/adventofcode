import java.io.File

val value = File("input/day2.txt").readText()
        .split(",")
        .toMutableList()
        .map { it.toInt() }
        .toIntArray()

value[1] = 12
value[2] = 2

for (i in 0..value.size-1 step 4) {
    when (value[i]) {
        1 -> {
            value[value[i + 3]] = value[value[i + 1]] + value[value[i + 2]]
        }
        2 -> {
            value[value[i + 3]] = value[value[i + 1]] * value[value[i + 2]]
        }
        99 -> {
            println("End: ${value[0]}")
            throw RuntimeException("done") // cause I'm lazy...
        }
    }
}