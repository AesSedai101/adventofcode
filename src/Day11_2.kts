
data class Scores(val score: Int, val allScores : MutableList<Pair<Int, Int>> = mutableListOf())

fun calculateScore(x: Int, y: Int, serialNumber: Int): Int {
    var powerLevel : Long = (x + 10L) * y
    powerLevel += serialNumber
    powerLevel *= (x + 10)
    powerLevel /= 100
    while (powerLevel >= 100) powerLevel -= 100
    while (powerLevel >= 10) powerLevel -= 10
    powerLevel -= 5
    return powerLevel.toInt()
}

val serialNumber = 5719
val totalGridSize = 300

val grid = (1..totalGridSize).flatMap { y -> (1..totalGridSize).map { x -> Pair(x, y) to Scores(calculateScore(x, y, serialNumber))}}.toMap()

grid.map {(coords, scores) ->
    scores.allScores.add(Pair(1, scores.score))
    var gridSize = 2
    var runningScore = scores.score
    while (gridSize <= 50) {
        /*
        running score is the total of the blocks marked X. just add the values of the .'s here
        XX.
        XX.
        ...
         */
        val x = coords.first + gridSize - 1
        val y = coords.second + gridSize - 1
        for (vx in coords.first..x) {
            runningScore += grid[Pair(vx, y)]?.score ?: 0
        }
        for (vy in coords.second..y-1) {
            runningScore += grid[Pair(x, vy)]?.score ?: 0
        }
        scores.allScores.add(Pair(gridSize, runningScore))
        gridSize++
    }
}

val coords = grid.maxBy { it.value.allScores.maxBy { it.second }!!.second }!!
println("${coords.key.first},${coords.key.second},${coords.value.allScores.maxBy { it.second }!!.first} = ${coords.value.allScores.maxBy { it.second }!!.second}")