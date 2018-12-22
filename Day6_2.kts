import java.io.File

data class Coords (val x: Int, val y: Int)
val file = File("input/day6.txt")

val points : MutableList<Coords>  = mutableListOf()

file.forEachLine {
    val (first, second) = it.split(",").map { it.trim().toInt() }
    points.add(Coords(first, second))
}

var minX = points.map { it.x }.min()!!
var maxX = points.map { it.x }.max()!!
var minY = points.map { it.y }.min()!!
var maxY = points.map { it.y }.max()!!

val maxDistance = 10000

println((minX..maxX)
        .flatMap {x ->
            (minY..maxY).map { Pair(x, it) }
        }
        .map {coord ->
            points.map { manhattanDistance(it, coord) }.sum()
        }.filter { it < maxDistance }
        .count()
)

fun manhattanDistance(p1: Coords, p2: Pair<Int, Int>): Int {
    return Math.abs(p1.x - p2.first) + Math.abs(p1.y - p2.second)
}