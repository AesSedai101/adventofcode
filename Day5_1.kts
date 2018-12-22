import java.io.File

val file = File("input/day5.txt")
val input = file.readLines().first()

val result = input.map { c -> c.toString() }.reduce { acc, c ->
    if (acc.isEmpty()) {
        c
    } else if (complements(acc.last(), c.toCharArray().first())) {
        acc.substring(0, acc.length - 1)
    } else {
        "$acc$c"
    }
}

println(result.length)

fun complements(one: Char, two: Char): Boolean {
    return one.toLowerCase() == two.toLowerCase() &&
            ((one.isUpperCase() && two.isLowerCase())
                    || (one.isLowerCase() && two.isUpperCase()))
}
