import java.io.File

data class Step(val name: String, val predecessors : MutableList<String>  = mutableListOf())

val file = File("input/day7.txt")

val steps = mutableListOf<Step>()

file.forEachLine {
    var line = it
    //Step A must be finished before step D can begin.
    line = line.replace("Step", "")
    line = line.replace("can begin.", "")
    line = line.replace(" must be finished before step ", ",").trim()
    val (first, second) = line.split(",").map { it.trim() }
    if (steps.find { it.name.equals(first) } == null) {
        steps.add(Step(first))
    }
    if (steps.find { it.name.equals(second) } == null) {
        steps.add(Step(second))
    }

    steps.find { it.name.equals(second) }!!.predecessors.add(first)
}

while(steps.size > 0) {
    val next = steps.filter { it.predecessors.isEmpty() }.sortedBy { it.name }.first()
    print(next.name)
    steps.remove(next)
    steps.forEach {
        it.predecessors.remove(next.name)
    }
}