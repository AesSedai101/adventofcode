import java.io.File

val file = File("input/day4.txt")
val input = file.readLines().sorted()

val sleepMap = HashMap<String, HashMap<Int, Int>>()
var activeGuard = ""
var awake = true
var start = 0

input.forEach {entry ->
    if (entry.contains("begins shift")) {
        // [1518-11-01 00:00] Guard #10 begins shift
        val split = entry.split("]")
        activeGuard = split[1].trim().replace("Guard", "").replace("begins shift", "").trim()

        awake = true
        start = 0
    } else if (entry.contains("falls asleep")) {
        // [1518-11-05 00:45] falls asleep
        awake = false
        start = entry.split("]")[0].split(":")[1].toInt()
    } else if (entry.contains("wakes up")) {
        // [1518-11-05 00:55] wakes up
        val end = entry.split("]")[0].split(":")[1].toInt()
        val currentSleep = sleepMap.getOrDefault(activeGuard, HashMap())

        for(i in start until end) {
            val c = currentSleep.getOrDefault(i, 0)
            currentSleep.put(i, c + 1)
        }

        sleepMap.put(activeGuard, currentSleep)
        awake = true
        start = 0
    }
}

// find the guard with the highest one
val guard = sleepMap.entries.sortedByDescending { it.value.values.max() }.first()

val guardNr = guard.key.replace("#", "").trim().toInt()
val minute = guard.value.entries.sortedByDescending { it.value }.first().key

println(guardNr*minute)