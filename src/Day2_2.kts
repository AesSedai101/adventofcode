import java.io.File
import kotlin.streams.toList

val file: File = File("../input/day2.txt")

val ids = file.inputStream().bufferedReader().lines().toList()

for (id in ids) {
    val optional = file.inputStream().bufferedReader().lines().filter {
        letterDifference(it, id) == 1
    }.findAny()

    if (optional!!.isPresent) {
        println((0 until id.length).filter { id[it] == optional.get()[it] }.map { id[it] }.joinToString(separator = ""))
        break
    }
}

fun letterDifference(s1: String, s2: String): Int {
    return (0 until s1.length).filter { s1[it] != s2[it] }.count()
}
