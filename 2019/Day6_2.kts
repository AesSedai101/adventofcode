import java.io.File
import java.lang.Exception

val lines = File("input/day6.txt").readLines()
    .map { it.trim() }

val planetList = mutableListOf<Planet>()

lines.forEach { orbit ->
    val (start, end) = orbit.split(")")

    var s = planetList.find { it.label == start }
    if (s == null) {
        s = Planet(start)
        planetList.add(s)
    }
    var e = planetList.find { it.label == end }
    if (e == null) {
        e = Planet(end)
        planetList.add(e)
    }

    s.orbitals.add(e)
}

val you: Planet = planetList.find { it.label.equals("YOU") }!!
val santa: Planet =  planetList.find { it.label.equals("SAN") }!!

val start = planetList.find { it.orbitals.contains(you) }!!
val target = planetList.find { it.orbitals.contains(santa) }!!

println("Finding shortest path from ${start.label} to ${target.label}")

// find the closest shared root
var root = target
while (distance(root, start) == -1 || distance(root, target) == -1) {
    root = planetList.find { it.orbitals.contains(root) }!!
}

println(distance(root, start) + distance(root, target))

data class Planet(val label: String, val orbitals: MutableList<Planet> = mutableListOf())

fun distance(root: Planet, target: Planet): Int {
    if (root.label == target.label) {
        return 0
    } else {
        for (p in root.orbitals) {
            val dist = distance(p, target)
            if (dist != -1) {
                return dist + 1
            }
        }
        return -1
    }
}
