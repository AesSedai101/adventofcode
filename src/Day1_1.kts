import java.io.File
import java.io.InputStream

val inputStream: InputStream = File("../input/day1_1.txt").inputStream()
println(inputStream.bufferedReader().lines().mapToInt { it.toInt() }.sum())
