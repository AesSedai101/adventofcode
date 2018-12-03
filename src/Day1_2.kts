import java.io.File
import java.io.InputStream

val file = File("../input/day1.txt")

val seen = HashSet<Int>()
var done = false
var frequency = 0

while (!done) {
    file.inputStream().bufferedReader().lines().mapToInt{ it.toInt() }.forEach {num ->
        frequency += num
        if (seen.contains(frequency)) {
            println (frequency)
            done = true
        }
        seen.add(frequency)
    }
}
