import java.io.File


val map = File("input/day10.txt")
        .readLines()
        .mapIndexed { i, s -> s.toCharArray().mapIndexed { j, c -> Triple(i, j, Space(c == '#')) }}
        .flatMap { it }
        .map { Pair(it.first, it.second) to it.third }
        .toMap()

// 0,0 top-left 1,0 top-left+1

val maxX = map.keys.map { it.first }.max()!!
val maxY = map.keys.map { it.second }.max()!!

for (x in 0..maxX) {
    for (y in 0..maxY) {
        if (map[Pair(x, y)]!!.hasAsteroid) {
            map[Pair(x, y)]!!.visibleAsteroids = countAsteroids(x, y, map)
        }
    }
}

// Print out the map
println()
for (x in 0..maxX) {
    for (y in 0..maxY) {
        if (map[Pair(x, y)]!!.hasAsteroid) {
            print(map[Pair(x, y)]!!.visibleAsteroids)
        } else {
            print('.')
        }
    }
    println()
}
println()

val bestPosition = map.filter { it.value.hasAsteroid }.map { Triple(it.value.visibleAsteroids, it.key.first, it.key.second) }.sortedBy { it.first }.last()
println("${bestPosition.third},${bestPosition.second} => ${bestPosition.first}")

fun countAsteroids(x: Int, y: Int, map: Map<Pair<Int, Int>, Space>): Int {
    return map.filter { it.value.hasAsteroid}
            .filter { it.key.first != x || it.key.second != y  }
            .map {
       calculateAngle(x, y, it.key.first, it.key.second)
    }.toSet().size
}

fun calculateAngle(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    var angle = Math.toDegrees(Math.atan2((x2 - x1).toDouble(), (y2 - y1).toDouble()))
    angle = angle + Math.ceil(-angle / 360) * 360
    return angle
}


data class Space(val hasAsteroid: Boolean, var visibleAsteroids: Int = 0)