import java.io.File
import java.lang.UnsupportedOperationException

enum class Direction {UP, DOWN, LEFT, RIGHT}
enum class Turn {LEFT, STRAIGHT, RIGHT}
data class Coords(val row: Int, val col: Int) {
    override fun toString(): String {
        return "$col,$row"
    }
}
data class Cart(val direction: Direction, val coords: Coords, val previousTurn: Turn) {
    override fun toString(): String {
        val  dir = when(direction)  {
            Direction.LEFT -> "<"
            Direction.RIGHT -> ">"
            Direction.DOWN -> "V"
            Direction.UP -> "^"
        }
        return "$coords:$dir"
    }
}
data class Track(val track: Char, val coords: Coords)
main()

fun main() {
    val (cartData, tracks) = readData()
    val carts = cartData.toMutableList()

    // move carts
    println("Starting with ${carts.size}")
    var tick = 0
    carts.sortWith(compareBy({it.coords.row}, {it.coords.col}))
    var cart = carts[0]

    while (carts.size > 1) {
        if (carts.indexOf(cart) == 0) {
            carts.sortWith(compareBy({it.coords.row}, {it.coords.col}))
            cart = carts[0]
            tick++
        }

        val newCart = moveCart(cart, tracks)
        val i = carts.indexOf(cart)
        carts.remove(cart)
        carts.add(i, newCart)

        val scrapYard = mutableListOf<Cart>()

        val collisions = carts.map { it.coords }.groupBy { it }.map { Pair(it.key, it.value.size) }.filter { it.second > 1 }
        collisions.forEach { collision ->
            val collisionCoords = collision.first
            val crashedCarts = carts.filter { it.coords == collisionCoords }
            scrapYard.addAll(crashedCarts)

            println("Crash on tick $tick at ${collisionCoords} with ${collision.second} cars " +
                    "$crashedCarts. ${carts.size - scrapYard.size} carts left: ")
        }

        if (scrapYard.size == 0) {
            var index = carts.indexOf(newCart) + 1
            val nextIndex = index % carts.size
            cart = carts[nextIndex]
        } else {
            var index = carts.indexOf(newCart) + 1
            while (scrapYard.contains(carts[index % carts.size])) {
                index ++
            }

            cart = carts[index % carts.size]

            carts.removeAll(scrapYard)
            println(carts)
        }

        if (carts.size == 1) {
            val lastCart = moveCart(carts[0],  tracks)
            println("Final cart remainig at ${lastCart.coords.col},${lastCart.coords.row}")
        }
    }
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

fun moveCart(cart: Day13_2.Cart, tracks: List<Track>): Day13_2.Cart {
    val nextCoords = when (cart.direction) {
        Day13_2.Direction.UP -> Day13_2.Coords(cart.coords.row - 1, cart.coords.col)
        Day13_2.Direction.DOWN -> Day13_2.Coords(cart.coords.row + 1, cart.coords.col)
        Day13_2.Direction.LEFT -> Day13_2.Coords(cart.coords.row, cart.coords.col - 1)
        Day13_2.Direction.RIGHT -> Day13_2.Coords(cart.coords.row, cart.coords.col + 1)
    }
    val newTrack = tracks.find { it.coords == nextCoords }!!
    var previousTurn = cart.previousTurn

    val direction: Day13_2.Direction = when (newTrack.track) {
        '|', '-' -> cart.direction
        '/' -> when (cart.direction) {
            Day13_2.Direction.UP -> Day13_2.Direction.RIGHT
            Day13_2.Direction.DOWN -> Day13_2.Direction.LEFT
            Day13_2.Direction.RIGHT -> Day13_2.Direction.UP
            Day13_2.Direction.LEFT -> Day13_2.Direction.DOWN
        }
        '\\' -> when (cart.direction) {
            Day13_2.Direction.UP -> Day13_2.Direction.LEFT
            Day13_2.Direction.DOWN -> Day13_2.Direction.RIGHT
            Day13_2.Direction.RIGHT -> Day13_2.Direction.DOWN
            Day13_2.Direction.LEFT -> Day13_2.Direction.UP
        }
        '+' -> {
            // turn order: left, straight, right
            previousTurn = when (cart.previousTurn) {
                Day13_2.Turn.LEFT -> Day13_2.Turn.STRAIGHT
                Day13_2.Turn.STRAIGHT -> Day13_2.Turn.RIGHT
                Day13_2.Turn.RIGHT -> Day13_2.Turn.LEFT
            }
            when (previousTurn) {
                Day13_2.Turn.STRAIGHT -> cart.direction
                Day13_2.Turn.LEFT -> when (cart.direction) {
                    Day13_2.Direction.UP -> Day13_2.Direction.LEFT
                    Day13_2.Direction.LEFT -> Day13_2.Direction.DOWN
                    Day13_2.Direction.DOWN -> Day13_2.Direction.RIGHT
                    Day13_2.Direction.RIGHT -> Day13_2.Direction.UP
                }
                Day13_2.Turn.RIGHT -> when (cart.direction) {
                    Day13_2.Direction.UP -> Day13_2.Direction.RIGHT
                    Day13_2.Direction.RIGHT -> Day13_2.Direction.DOWN
                    Day13_2.Direction.DOWN -> Day13_2.Direction.LEFT
                    Day13_2.Direction.LEFT -> Day13_2.Direction.UP
                }
            }
        }
        else -> throw UnsupportedOperationException("Unknown track type [${newTrack.track}]")
    }
    val newCart = Day13_2.Cart(direction, nextCoords, previousTurn)
    return newCart
}

fun readData(): Pair<List<Cart>, List<Track>> {
    var carts = mutableListOf<Day13_2.Cart>()
    val tracks = mutableListOf<Day13_2.Track>()

    // build up data
    var row = 0
    File("input/day13.txt").forEachLine { line ->
        var col = 0
        line.forEach { c ->
            var char = c
            if (char != ' ') {
                if (char == '^') {
                    carts.add(Day13_2.Cart(Day13_2.Direction.UP, Day13_2.Coords(row, col), Day13_2.Turn.RIGHT))
                    char = '|'
                } else if (char == 'v') {
                    carts.add(Day13_2.Cart(Day13_2.Direction.DOWN, Day13_2.Coords(row, col), Day13_2.Turn.RIGHT))
                    char = '|'
                } else if (char == '<') {
                    carts.add(Day13_2.Cart(Day13_2.Direction.LEFT, Day13_2.Coords(row, col), Day13_2.Turn.RIGHT))
                    char = '-'
                } else if (char == '>') {
                    carts.add(Day13_2.Cart(Day13_2.Direction.RIGHT, Day13_2.Coords(row, col), Day13_2.Turn.RIGHT))
                    char = '-'
                }
                tracks.add(Day13_2.Track(char, Day13_2.Coords(row, col)))
            }
            col++
        }
        row++
    }
    return Pair(carts, tracks)
}