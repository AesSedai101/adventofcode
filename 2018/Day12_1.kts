import java.io.File


val inputLines = File("input/day12.txt").readLines()

var plantMap = mutableMapOf<Int, Boolean>()
plantMap.putAll(inputLines[0].replace("initial state:", "").trim()
        .map { it == '#' }.mapIndexed{i, b -> i to b}.toMap())

val patterns = mutableMapOf<String, Boolean>()

inputLines.subList(1, inputLines.size).filter { !it.isEmpty() }.forEach { line ->
    //...## => #
    val lines = line.split("=>").map { it.trim() }
    patterns[lines[0]] = lines[1].first() == '#'
}

(1..20).forEach { _ ->

    val min = plantMap.keys.min()!!
    if (plantMap[min]!! || plantMap[min + 1]!!) {
        plantMap.put(min - 1, false)
        plantMap.put(min-2, false)
    }
    val max = plantMap.keys.max()!!
    if (plantMap[max]!! || plantMap[max-1]!!) {
        plantMap.put(max + 1, false)
        plantMap.put(max + 2, false)
    }

    var newPlants = mutableMapOf<Int, Boolean>()

    plantMap.forEach {index, _ ->
        val pattern = BooleanArray(5)
        pattern[0] = plantMap.getOrDefault(index - 2, false)
        pattern[1] = plantMap.getOrDefault(index - 1, false)
        pattern[2] = plantMap.getOrDefault(index, false)
        pattern[3] = plantMap.getOrDefault(index + 1, false)
        pattern[4] = plantMap.getOrDefault(index + 2, false)
        val patternString = pattern.map { if (it) '#' else '.' }.joinToString("")

        val newValue = patterns.getOrDefault(patternString, false)
        newPlants.put(index, newValue)
    }
    plantMap = newPlants
}

println(plantMap.entries.map { (idx, plant) -> if (plant) idx else 0 }.sum())