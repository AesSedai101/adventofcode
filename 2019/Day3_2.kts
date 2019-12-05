import java.io.File

val lines = File("input/day3.txt").readLines()

val one = instructionSet(lines[0])
val two = instructionSet(lines[1])

val w1 = points(one)
val w2 = points(two)

val intersections = w1.intersect(w2)

println(intersections.map { w1.indexOf(it) + w2.indexOf(it) }.sorted().first() + 2)

fun points(instructions:  List<Pair<Char, Int>>) : List<Pair<Int, Int>> {
    var x = 0
    var y = 0

    val points = mutableListOf<Pair<Int, Int>>()

    for (instr in instructions) {
        when (instr.first) {
            'R' -> {
                // move x positive
                for (xi in x + 1..x + instr.second) {
                    points.add(Pair(xi, y))
                }
                x = x + instr.second
            }
            'L' -> {
                // move x negative
                for (xi in x - 1 downTo x - instr.second) {
                    points.add(Pair(xi, y))
                }
                x = x - instr.second
            }
            'U' -> {
                // move y positive
                for (yi in y + 1..y + instr.second) {
                    points.add(Pair(x, yi))
                }
                y = y + instr.second
            }
            'D' -> {
                // move y negative
                for (yi in y - 1 downTo y - instr.second) {
                    points.add(Pair(x, yi))
                }
                y = y - instr.second
            }
        }
    }

    return points.toList()
}

fun instructionSet(lines: String): List<Pair<Char, Int>> {
    return lines.split(",").map { it -> Pair(it[0], it.substring(1).toInt()) }
}