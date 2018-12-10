import java.io.File

class Point(val x: Long, val y: Long, val velocity:Pair<Long, Long>) {
    override fun toString(): String {
        return "($x, $y) -> $velocity"
    }

    fun tick() : Point {
        return Point(x + velocity.first, y + velocity.second, velocity)
    }
}

fun area(points : List<Point>): Long {
    val minX = points.minBy { it.x }!!.x
    val maxX = points.maxBy { it.x }!!.x

    val minY = points.minBy { it.y }!!.y
    val maxY = points.maxBy { it.y }!!.y

    return (maxX - minX + 1) * (maxY - minY + 1)
}

val input = File("../input/day10.txt")
        .readLines()
        .map {line ->

            val position = line.substring(line.indexOf("<") + 1, line.indexOf(">"))
            val (x, y) = position.split(",").map { it.trim().toLong() }

            val (vX, vY) = line.substring(line.lastIndexOf("<") + 1, line.lastIndexOf(">")).split(",").map { it.trim().toLong() }
            Point(x, y, Pair(vX, vY))
        }

var points = input
println("Points: ${points.size}")

var area = Long.MAX_VALUE
var tick = 0
while(true) {
    val afterTick = points.map { it.tick() }
    val newArea = area(afterTick)

    if (newArea < area) {
        points = afterTick
        area = newArea
        tick ++
    } else {
        println("Done! $tick")
        println(visualize(points))
        break
    }
}

fun visualize(points: List<Point>): String {
    val minX = points.minBy { it.x }!!.x
    val maxX = points.maxBy { it.x }!!.x
    val minY = points.minBy { it.y }!!.y
    val maxY = points.maxBy { it.y }!!.y

    var stars = ""
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (points.find { it.x == x && it.y == y } != null) {
                stars += '#'
            } else {
                stars += '.'
            }
        }
        stars += '\n'
    }
    return stars
}
