class Marble(val value : Int) {
    var prev: Marble? = null
    var next: Marble? = null
}

val playerCount = 473
val lastMarble = 70904 * 100

val playerScores = LongArray(playerCount) {0L}

val zeroMarble = Marble(0)
var currentMarble = Marble(1)

zeroMarble.next = currentMarble
zeroMarble.prev = currentMarble

currentMarble.next = zeroMarble
currentMarble.prev = zeroMarble

var activePlayer = 0

for (marble in 2..lastMarble) {

    if (marble % 23 == 0) {
        playerScores[activePlayer] += marble.toLong()
        (1..7).forEach{ currentMarble = currentMarble.prev!! }
        playerScores[activePlayer] += currentMarble.value.toLong()

        val before = currentMarble.prev!!
        val after = currentMarble.next!!
        before.next = after
        after.prev = before
        currentMarble = after
    } else {
        val newMarble = Marble(marble)

        currentMarble = currentMarble.next!!
        val nextMarble = currentMarble.next!!

        currentMarble.next = newMarble
        newMarble.next = nextMarble

        nextMarble.prev = newMarble
        newMarble.prev = currentMarble

        currentMarble = newMarble
    }
    activePlayer = (activePlayer + 1) % playerCount
}

println("\nHighest score = " + playerScores.max())

fun marbleString(zeroMarble: Marble, currentMarble: Marble): String {
    var result = ""

    var thisMarble = zeroMarble
    do {
        if (thisMarble == currentMarble) {
            result += "(${thisMarble.value})"
        } else {
            result += " ${thisMarble.value} "
        }
        thisMarble = thisMarble.next!!
    } while (thisMarble != zeroMarble);


    return result
}
