import java.io.File

val file = File("../input/day3.txt")

val fabric = HashMap<Pair<Int, Int>, HashSet<String>>()

file.inputStream().bufferedReader().forEachLine { line ->
    val idValueSplit = line.split('@')

    val id = idValueSplit[0].trim()
    val values = idValueSplit[1].trim().split(':')
    val (left, top) = values[0].trim().split(",").map { it.trim().toInt() }
    val (width, height) = values[1].trim().split('x').map { it.trim().toInt() }

    for(row in left until left+width) {
        for (col in top until top+height) {
            val coord = Pair(row, col)
            val set = fabric.getOrDefault(coord, HashSet())
            set.add(id)
            fabric.put(coord, set)
        }
    }
}

val allIds = fabric.values.flatMap { it }.toHashSet()
allIds.removeAll(fabric.values.filter { it.size > 1 }.flatMap { it }.toHashSet())
println(allIds)
