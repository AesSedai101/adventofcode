import java.io.File

val lines = File("input/day3.txt").readLines()

/*

R8,U5,L5,D3
U7,R6,D4,L4 = distance 6

R75,D30,R83,U83,L12,D49,R71,U7,L72
U62,R66,U55,R34,D71,R55,D58,R83 = distance 159

R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51
U98,R91,D20,R16,D67,R40,U7,R15,U6,R7 = distance 135
 */

val one = instructionSet(lines[0])
val two = instructionSet(lines[1])

val wires = arrayOf(one, two)
val grid = mutableMapOf</*Coords*/Pair<Int, Int>, /*list of wires that crosses this block */MutableList<Int>>()

for ((index, instructions) in wires.withIndex()) {
    var x = 0
    var y = 0

    for (instr in instructions) {
        when(instr.first) {
            'R' -> {
                // move x positive
                for (xi in x+1..x+instr.second) {
                    var list = grid[Pair(xi, y)]
                    if (list == null) {
                        list = mutableListOf<Int>()
                        grid[Pair(xi, y)] = list!!
                    }
                    grid[Pair(xi, y)]?.add(index)
                }
                x = x + instr.second
            }
            'L' -> {
                // move x negative
                for (xi in x-1 downTo x-instr.second) {
                    var list = grid[Pair(xi, y)]
                    if (list == null) {
                        list = mutableListOf<Int>()
                        grid[Pair(xi, y)] = list!!
                    }
                    grid[Pair(xi, y)]?.add(index)
                }
                x = x - instr.second
            }
            'U' -> {
                // move y positive
                for (yi in y+1..y+instr.second) {
                    var list = grid[Pair(x, yi)]
                    if (list == null) {
                        list = mutableListOf<Int>()
                        grid[Pair(x, yi)] = list!!
                    }
                    grid[Pair(x, yi)]?.add(index)
                }
                y = y + instr.second
            }
            'D' -> {
                // move y negative
                for (yi in y-1 downTo y-instr.second) {
                    var list = grid[Pair(x, yi)]
                    if (list == null) {
                        list = mutableListOf<Int>()
                        grid[Pair(x, yi)] = list!!
                    }
                    grid[Pair(x, yi)]?.add(index)
                }
                y = y - instr.second
            }
        }
    }
}

println(grid
        .filter { (_,v) -> v.toSet().size > 1 }
        .keys
        .map { (a, b) -> Math.abs(a) + Math.abs(b) }
        .sorted()
        .first() )

fun instructionSet(lines: String): List<Pair<Char, Int>> {
    return lines.split(",").map { it -> Pair(it[0], it.substring(1).toInt()) }
}

fun printGrid(grid: Map<Pair<Int, Int>, MutableList<Int>>) {
    val minX = grid.keys.map { it.first }.min()!!
    val maxX = grid.keys.map { it.first }.max()!!
    val minY = grid.keys.map { it.second }.min()!!
    val maxY = grid.keys.map { it.second }.max()!!

    println("X: $minX - $maxX, Y: $minY - $maxY")

    for (y in maxY+1 downTo minY-1) {
        println(' ')
        for (x in minX-1..maxX+1) {
            val value = grid[Pair(x, y)]?.toSet()?.size
            if (x != 0 || y!= 0) {
                if (value == null) {
                    print('.')
                } else if (value > 1) {
                    print('X')
                } else {
                    if (grid[Pair(x-1, y)] != null || grid[Pair(x+1, y)] != null) print('-')
                    else print('|')
                }
            } else {
                if (value == null) {
                    print('o')
                } else if (value > 1) {
                    print('H')
                } else {
                    print('0')
                }
            }
        }
    }
    println(' ')
}