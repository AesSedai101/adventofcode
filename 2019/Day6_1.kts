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

val orbiting: Set<Planet> = planetList.flatMap { it.orbitals }.toSet()
val root: Planet = planetList.toSet().minus(orbiting).first()

var sum = 0
for (planet in planetList) {
    sum += distance(root, planet)
}
println(sum)

fun distance(root: Planet, target: Planet): Int {
    if (root.label == target.label) {
        return 0
    } else if (root.orbitals.size == 0) {
        return -1
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

data class Planet(val label: String, val orbitals: MutableList<Planet> = mutableListOf())

