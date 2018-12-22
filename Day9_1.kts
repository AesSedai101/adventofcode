val playerCount = 473
val lastMarble = 70904

val playerScores = IntArray(playerCount) {0}
val marbles = mutableListOf(0)

//println("[-] " + marbles.mapIndexed { _, i -> "($i)" }.joinToString(""))

var activePlayer = 0
var currentMarble = 0

for (marble in 1..lastMarble) {

    if (marble % 23 == 0) {
        playerScores[activePlayer] += marble
        val remove = (currentMarble - 7 + marbles.size) % marbles.size
        playerScores[activePlayer] += marbles.removeAt(remove)
        currentMarble = remove
    } else {
        val position = (currentMarble + 1) % marbles.size + 1
        marbles.add(position, marble)
        currentMarble = position
    }
    activePlayer = (activePlayer + 1) % playerCount

    //println("[$activePlayer] " + marbles.mapIndexed{index, value -> if (index == currentMarble) "($value)" else " $value "}.joinToString(""))
}

println("\nHighest score = " + playerScores.max())