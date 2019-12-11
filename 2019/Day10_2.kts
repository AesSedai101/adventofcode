import java.io.File


val spaceMap = File("input/day10.txt")
        .readLines()
        .mapIndexed { i, s -> s.toCharArray().mapIndexed { j, c -> Triple(j, i,c == '#') }}
        .flatMap { it }
        .map { Space(it.first, it.second, it.third) }

// 0,0 top-left 1,0 top-left+1

val maxX = spaceMap.map { it.x }.max()!!
val maxY = spaceMap.map { it.y }.max()!!

for (x in 0..maxX) {
    for (y in 0..maxY) {
        if (spaceMap.find { it.x == x && it.y == y }!!.hasAsteroid) {
            spaceMap.find { it.x == x && it.y == y }!!.visibleAsteroids = countAsteroids(x, y, spaceMap)
        }
    }
}

val bestPosition = spaceMap.filter { it.hasAsteroid }.sortedBy { it.visibleAsteroids }.last()
println("Deploying to ${bestPosition.x},${bestPosition.y} => ${bestPosition.visibleAsteroids} asteroids visible")

var remainingAsteroids = spaceMap.filter { it.hasAsteroid }
        .filter { it.x != bestPosition.x || it.y != bestPosition.y }
        .map { it }
        .groupBy {
            val angle = calculateAngle(it.x, it.y, bestPosition.x, bestPosition.y)
            if (angle == 0.0) angle else 360 - angle
        }
        .toSortedMap()
        .map { it.value.sortedBy { distance(bestPosition.x, bestPosition.y, it.x, it.y) }.toMutableList() }
        .toMutableList()

println(remainingAsteroids)

var vaporised = 0
var n200: Space? = null

while(!remainingAsteroids.isEmpty()) {
    println("${remainingAsteroids.size} asteroids remaining. Vaporised: $vaporised")

    for (t in remainingAsteroids) {
        val removeAt = t.removeAt(0)
        vaporised++
        println("Vaporising $vaporised'th asteroid at (${removeAt.x},${removeAt.y})")
        if (vaporised == 200) {
            n200 = removeAt
        }
    }

    remainingAsteroids = remainingAsteroids.filter { it.size > 0 }.toMutableList()
}

println("The answer is: ${n200!!.x * 100 + n200!!.y}")

fun countAsteroids(x: Int, y: Int, map: List<Space>): Int {
    return map.filter { it.hasAsteroid}
            .filter { it.x != x || it.y != y  }
            .map {
       calculateAngle(x, y, it.x, it.y)
    }.toSet().size
}

fun calculateAngle(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    var angle = Math.toDegrees(Math.atan2((x2 - x1).toDouble(), (y2 - y1).toDouble()))
    angle = angle + Math.ceil(-angle / 360) * 360
    return angle
}

fun distance(x1: Int, y1: Int, x2: Int, y2: Int): Double {
    return Math.sqrt((y2 - y1).toDouble() * (y2 - y1).toDouble() + (x2 - x1).toDouble() * (x2 - x1).toDouble());
}

data class Space(val x: Int, val y: Int, val hasAsteroid: Boolean, var visibleAsteroids: Int = 0)