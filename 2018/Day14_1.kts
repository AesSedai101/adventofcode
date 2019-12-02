
intArrayOf(9, 5, 18, 2018, 598701).forEach {
    val calc = calculate(it)
    println("$it: ${calc}")
}

fun printScores(recipes: String, elf1: Int, elf2: Int) {
    for(i in 0 until recipes.length) {
        if (i == elf1) {
            print("(${recipes[i]})")
        } else if (i == elf2) {
            print("[${recipes[i]}]")
        } else {
            print(" ${recipes[i]} ")
        }
    }
    println()
}

fun calculate(iterations: Int): String {
    var scores = "37"

    var elf1 = 0
    var elf2 = 1

    while (scores.length < iterations) {
        //printScores(scores, elf1, elf2)

        val elf1Val = scores[elf1].toString().toInt()
        val elf2Val = scores[elf2].toString().toInt()
        val sum = elf1Val + elf2Val

        scores = scores + sum.toString()

        elf1 = (elf1 + 1 + elf1Val) % scores.length
        elf2 = (elf2 + 1 + elf2Val) % scores.length
    }
    val input = scores.substring(0, iterations)

    for (i in 0..10) {
        //printScores(scores, elf1, elf2)

        val elf1Val = scores[elf1].toString().toInt()
        val elf2Val = scores[elf2].toString().toInt()
        val sum = elf1Val + elf2Val

        scores = scores + sum.toString()

        elf1 = (elf1 + 1 + elf1Val) % scores.length
        elf2 = (elf2 + 1 + elf2Val) % scores.length
    }

    return (scores.replaceFirst(input, "").substring(0, 10))
}