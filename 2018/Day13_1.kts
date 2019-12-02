import java.io.File
import java.lang.UnsupportedOperationException

enum class Direction {UP, DOWN, LEFT, RIGHT}
enum class Turn {LEFT, STRAIGHT, RIGHT}
data class Coords(val row: Int, val col: Int)
data class Cart(val direction: Direction, val coords: Coords, val previousTurn: Turn)
data class Track(val track: Char, val coords: Coords)

var carts = mutableListOf<Cart>()
val tracks = mutableListOf<Track>()

// build up data
var row = 0
File("input/day13.txt").forEachLine {line ->
    var col = 0
    line.forEach { c ->
        var char = c
        if (char != ' ') {
            if (char == '^') {
                carts.add(Cart(Direction.UP, Coords(row, col), Turn.RIGHT))
                char = '|'
            } else if (char == 'v') {
                carts.add(Cart(Direction.DOWN, Coords(row, col), Turn.RIGHT))
                char = '|'
            } else if (char == '<') {
                carts.add(Cart(Direction.LEFT, Coords(row, col), Turn.RIGHT))
                char = '-'
            } else if (char == '>') {
                carts.add(Cart(Direction.RIGHT, Coords(row, col), Turn.RIGHT))
                char = '-'
            }
            tracks.add(Track(char, Coords(row, col)))
        }
        col++
    }
    row++
}
//printTracks(tracks, carts)

// move carts
var crashed = false
var tick = 0
while (!crashed) {
    carts.sortedWith(compareBy({it.coords.row}, {it.coords.col})).forEach { cart ->
        val nextCoords = when (cart.direction) {
            Direction.UP -> Coords(cart.coords.row-1, cart.coords.col)
            Direction.DOWN -> Coords(cart.coords.row+1, cart.coords.col)
            Direction.LEFT -> Coords(cart.coords.row, cart.coords.col-1)
            Direction.RIGHT -> Coords(cart.coords.row, cart.coords.col+1)
        }
        val newTrack = tracks.find { it.coords == nextCoords }!!
        var previousTurn = cart.previousTurn

        val direction : Direction = when (newTrack.track) {
            '|', '-' -> cart.direction
            '/' -> when (cart.direction) {
                Direction.UP -> Direction.RIGHT
                Direction.DOWN -> Direction.LEFT
                Direction.RIGHT -> Direction.UP
                Direction.LEFT -> Direction.DOWN
            }
            '\\' -> when (cart.direction) {
                Direction.UP -> Direction.LEFT
                Direction.DOWN -> Direction.RIGHT
                Direction.RIGHT -> Direction.DOWN
                Direction.LEFT -> Direction.UP
            }
            '+' -> {
                // turn order: left, straight, right
                previousTurn =  when (cart.previousTurn) {
                    Turn.LEFT -> Turn.STRAIGHT
                    Turn.STRAIGHT -> Turn.RIGHT
                    Turn.RIGHT -> Turn.LEFT
                }
                when (previousTurn) {
                    Turn.STRAIGHT -> cart.direction
                    Turn.LEFT -> when (cart.direction) {
                        Direction.UP -> Direction.LEFT
                        Direction.LEFT -> Direction.DOWN
                        Direction.DOWN -> Direction.RIGHT
                        Direction.RIGHT -> Direction.UP
                    }
                    Turn.RIGHT -> when (cart.direction) {
                        Direction.UP -> Direction.RIGHT
                        Direction.RIGHT -> Direction.DOWN
                        Direction.DOWN -> Direction.LEFT
                        Direction.LEFT -> Direction.UP
                    }
                }
            }
            else -> throw UnsupportedOperationException("Unknown track type [${newTrack.track}]")
        }

        carts.remove(cart)
        carts.add(Cart(direction, nextCoords, previousTurn))

        val collision = carts.map { it.coords }.groupBy { it }.map { Pair(it.key, it.value.size) }.filter { it.second > 1 }.firstOrNull()
        if (collision != null) {
            crashed = true
            println("Crash on tick $tick")
            val collisionCoords = collision.first
            printTracks(tracks, carts, collisionCoords)
            println("${collisionCoords.col},${collisionCoords.row}")
            throw UnsupportedOperationException("done");
        }
    }

    tick++
}

fun printTracks(tracks: List<Track>, carts: List<Cart>, crash: Coords = Coords(0,0)) {
    val maxRow = tracks.maxBy { it.coords.row }!!.coords.row
    val maxCol = tracks.maxBy { it.coords.col }!!.coords.col
    for (row in 0..maxRow) {
        for (col in 0..maxCol) {
            val track = tracks.find { it.coords == Coords(row, col) }
            val cart = carts.find { it.coords == Coords(row, col) }
            if (Coords(row, col) == crash) {
                print('X')
            } else if ( track != null ) {
                if (cart != null) {
                    when(cart.direction) {
                        Direction.UP -> print('^')
                        Direction.DOWN -> print('v')
                        Direction.LEFT -> print('<')
                        Direction.RIGHT -> print('>')
                    }
                } else {
                    print(track.track)
                }
            } else {
                print(' ')
            }
        }
        println()
    }
}