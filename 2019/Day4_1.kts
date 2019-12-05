import java.io.File

val (min, max) = File("input/day4.txt").readText().split("-").map { it.toLong() }

println("$min -> $max")

val count = (min..max) // the num is in the range
        .map { it.toString().toCharArray() }
        /* --- there are two adjacent digits --- */
        // filter out all the results with 6 distinct characters
        .filter { it.distinct().size < 6 }
        // check if the same chars are adjacen
        .filter { it.joinToString("").zipWithNext().filter { it.first == it.second }.isNotEmpty() }
        /* --- the digits are strictly ascending --- */
        .filter {
            val original: String = it.joinToString("")
            val sorted: String  = it.map { it.toString().toInt() }.sorted().joinToString("")
            original.equals(sorted)
        }
        .count()

println(count)

