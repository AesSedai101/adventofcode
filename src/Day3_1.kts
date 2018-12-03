import java.io.File

val file = File("input/day3.txt")

val fabric = HashMap<Pair<Int, Int>, Int>()

file.inputStream().bufferedReader().forEachLine { line ->
    val values = line.split('@')[1].trim().split(':')
    val (left, top) = values[0].trim().split(",").map { it.trim().toInt() }
    val (width, height) = values[1].trim().split('x').map { it.trim().toInt() }

    for(row in left until left+width) {
        for (col in top until top+height) {
            val coord = Pair(row, col)
            val count = fabric.getOrDefault(coord, 0)
            fabric.put(coord, count + 1)
        }
    }
}

println(fabric.filterValues { it > 1 }.count())