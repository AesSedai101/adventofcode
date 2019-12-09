import java.io.File
import java.lang.Exception

val width = 25
val height = 6

val result = File("input/day8.txt")
        .readText()
        .trim()
        .toList()
        .chunked(width * height)
        .reduce { list1, list2 ->
            val result = mutableListOf<Char>()
            for (i in 0..(width*height-1)) {
                result.add(if (list1[i] != '2') list1[i] else list2[i])
            }
            result
        }

for(i in 0..26) print("█")
println()

result.chunked(width).forEach {
    print("█")
    it.forEach { c ->
        if (c == '0') print("█") else print (" ")
    }
    println("█")
}

for(i in 0..26) print("█")
println()