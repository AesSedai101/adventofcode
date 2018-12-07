import java.io.File

val num_workers = 5

val steps = readSteps()
val workers = mutableListOf<Worker>()

for (i in 0 until num_workers) {
    workers.add(Worker(i))
}

var ticks = 0
while(steps.size > 0 || workers.filter { it.isAvailable() }.size != workers.size) {
    // get all the available steps
    val availableSteps = steps.filter { it.predecessors.isEmpty() }.sortedBy { it.name }
    availableSteps.forEach { step ->
        // get the first available worker, if there is one
        val worker = workers.filter { it.isAvailable() }.firstOrNull()
        if (worker != null) {
            // assign the step to the worker
            worker.step = step
            worker.tick = step.cost()
            // remove the step from the list
            steps.remove(step)
        }
    }

    // tick every worker
    workers.forEach{
        if (it.tick()) {
            val name = it.step!!.name
            // release the predecessors
            steps.forEach {
                it.predecessors.remove(name)
            }
            it.step = null
        }
    }
    ticks++
}
println(ticks)

class Worker(val num: Int) {
    var step: Step? = null
    var tick: Int = 0

    fun tick(): Boolean {
        if (tick > 0) {
            tick --
            if (tick == 0) {
                return true
            }
        }
        return false
    }

    fun isAvailable() : Boolean {
        return step == null || tick == 0
    }

    override fun toString(): String {
        return "Worker [$num]: { tick = ${tick}, step = ${step} }"
    }
}

data class Step(val name: String, val predecessors : MutableList<String>  = mutableListOf()){
    val static_cost = 60

    fun cost() : Int {
       return (name.toLowerCase()[0].toInt() - 96) + static_cost
    }
}

fun readSteps() : MutableList<Step> {
    val file = File("../input/day7.txt")

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
    return steps
}


