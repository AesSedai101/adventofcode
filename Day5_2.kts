import java.io.File

val file = File("input/day5.txt")
val input = file.readLines().first()

val units = input.toLowerCase().toCharArray().distinct()

var smallest = Integer.MAX_VALUE
units.forEach { unit ->
    val cleanedInput = input.filter { it.toLowerCase() != unit }
    val result = cleanedInput.map { c -> c.toString() }.reduce { acc, c ->
        if (acc.isEmpty()) {
            c
        } else if (complements(acc.last(), c.toCharArray().first())) {
            acc.substring(0, acc.length - 1)
        } else {
            "$acc$c"
        }
    }

    if (result.length < smallest) {
        smallest = result.length
    }
}

println(smallest)

fun complements(one: Char, two: Char): Boolean {
    return one.toLowerCase() == two.toLowerCase() &&
            ((one.isUpperCase() && two.isLowerCase())
                    || (one.isLowerCase() && two.isUpperCase()))
}
