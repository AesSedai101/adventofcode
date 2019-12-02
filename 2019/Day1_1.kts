import java.io.File
import java.io.InputStream

val inputStream: InputStream = File("input/day1.txt").inputStream()

println(
        inputStream.bufferedReader().lines()
                .mapToDouble { it.toDouble() }
                .map { it / 3.0 }
                .map { Math.floor(it) }
                .map { it - 2 }
                .sum()
)