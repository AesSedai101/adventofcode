import java.io.File

data class Coords (val x: Int, val y: Int, val closest: Pair<Int, Int>)
data class CoordsArea (val coords: Pair<Int, Int>, val areaSize: Int)

val file = File("../input/day6.txt")

val points : MutableList<Coords>  = mutableListOf()

file.forEachLine {
    val (first, second) = it.split(",").map { it.trim().toInt() }
    points.add(Coords(first, second, Pair(first, second)))
}

println("Points: ${points.size}")

val timeMap : MutableList<Coords>  = mutableListOf()
// put the actual points on the map
points.forEach {
    timeMap.add(it)
}

var minX = timeMap.map { it.x }.min()!!
var maxX = timeMap.map { it.x }.max()!!
var minY = timeMap.map { it.y }.min()!!
var maxY = timeMap.map { it.y }.max()!!

println("Bounds: ($minX, $minY), ($maxX, $maxY)")

for (x in minX..maxX) {
    for (y in minY..maxY) {
        if (timeMap.find { it.x == x && it.y == y } == null) {
            val nearest = points.groupBy { manhattanDistance(it, Pair(x, y)) }.minBy { it.key }!!

            var closest : Pair<Int, Int>;
            if (nearest.value.size > 1) {
                closest = Pair(Int.MAX_VALUE, Int.MAX_VALUE)
            } else {
                val n = nearest.value.first()
                closest = Pair(n.x, n.y)
            }

            timeMap.add(Coords(x,y, closest))
        }
    }
}

// points on the edges
val edges = mutableSetOf<Pair<Int, Int>>()
for (x in minX..maxX) {
    edges.add(timeMap.find { it.x == x && it.y == minY }!!.closest)
    edges.add(timeMap.find { it.x == x && it.y == maxY }!!.closest)
}
for (y in minY..maxY) {
    edges.add(timeMap.find { it.y == y && it.x == minX }!!.closest)
    edges.add(timeMap.find { it.y == y && it.x == maxX }!!.closest)
}

println("Edges: ${edges.size}")

val result = timeMap
        .filter { it.closest !=  Pair(Int.MAX_VALUE, Int.MAX_VALUE)}
        .groupBy { it.closest }
        .map { CoordsArea(Pair(it.key.first, it.key.second), it.value.count()) }
        .filter { !edges.contains(it.coords) }
        .sortedByDescending { it.areaSize }
        .first()

println(result)

fun manhattanDistance(p1: Coords, p2: Pair<Int, Int>): Int {
    return Math.abs(p1.x - p2.first) + Math.abs(p1.y - p2.second)
}
