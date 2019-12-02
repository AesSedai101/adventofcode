import java.io.File
import java.io.InputStream

val file: File = File("input/day2.txt")

val twoLetters = file.inputStream().bufferedReader().lines().filter {
    it.groupingBy { it }.eachCount().containsValue(2)
}.count();

val threeLetters = file.inputStream().bufferedReader().lines().filter {
    it.groupingBy { it }.eachCount().containsValue(3)
}.count();

println(twoLetters * threeLetters)