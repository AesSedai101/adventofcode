import java.io.File

val result = File("input/day8.txt")
        .readText()
        .trim()
        .toList()
        .chunked(25 * 6)
        .sortedBy {
            it.filter { it.equals('0') }.count()
        }
        .first()

println(result.count { it.equals('1') } * result.count { it.equals('2') } )