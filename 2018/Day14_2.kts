
arrayOf("51589", "01245", "92510", "59414", "598701").forEach {
    val calc = calculate(it)
    println("$it: ${calc}")
}

fun calculate(digits: String): Int {
    var scores = intArrayOf(3, 7)

    var elf1 = 0
    var elf2 = 1

    while (true) {
        val elf1Val = scores[elf1]
        val elf2Val = scores[elf2]
        val sum = elf1Val + elf2Val

        scores = scores + sum.toString().map { it.toString().toInt() }

        elf1 = (elf1 + 1 + elf1Val) % scores.size
        elf2 = (elf2 + 1 + elf2Val) % scores.size

        if (scores.takeLast(digits.length + 5).joinToString("").contains(digits)) {
            println()
            return scores.joinToString("").indexOf(digits)
        } else {
            print(".")
        }
    }
}