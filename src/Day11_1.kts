import com.sun.prism.image.Coords

data class Point(val x: Int, val y: Int, val score: Int)

fun calculateScore(x: Int, y: Int, serialNumber: Int): Int {
    //Find the fuel cell's rack ID, which is its X coordinate plus 10.
    val rackId = x + 10;
    //Begin with a power level of the rack ID times the Y coordinate.
    var powerLevel = rackId * y
    //Increase the power level by the value of the grid serial number (your puzzle input).
    powerLevel += serialNumber
    //Set the power level to itself multiplied by the rack ID.
    powerLevel *= rackId
    //Keep only the hundreds digit of the power level (so 12345 becomes 3; numbers with no hundreds digit become 0).
    powerLevel /= 100
    while (powerLevel >= 10) powerLevel -= 10
    //Subtract 5 from the power level.
    powerLevel -= 5
    return powerLevel
}

val serialNumber = 5719
val gridSize = 300
val grid = (1..gridSize).flatMap { y -> (1..gridSize).map { x ->  Point(x, y, calculateScore(x, y, serialNumber))}}

val max = grid.maxBy { coords ->
    var count = 0
    for(x in coords.x.. coords.x+2) {
        for (y in coords.y..coords.y+2) {
            val firstOrNull = grid.filter { it.x == x && it.y == y }.firstOrNull()
            if (firstOrNull != null) {
                count += firstOrNull.score
            }
        }
    }
    count
}

println(max)
