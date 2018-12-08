import java.io.File

data class TreeNode(val children: List<TreeNode>, val metaData: List<Int> )

val input = File("../input/day8.txt").bufferedReader()
        .readLine()
        .split(" ")
        .map { it.toInt() }

val tree = buildTree(input).first
println(value(tree))

fun buildTree(inputList: List<Int>): Pair<TreeNode, List<Int>> {
    var input = inputList.toMutableList()
    //child nodes
    val childCount = input.removeAt(0)
    val metaCount = input.removeAt(0)

    val children = mutableListOf<TreeNode>()
    for (i in 1..childCount){
        val result = buildTree(input)
        children.add(result.first)
        input = result.second.toMutableList()
    }
    val metaData = mutableListOf<Int>()
    for(i in 1..metaCount) {
        metaData.add(input.removeAt(0))
    }
    return Pair(TreeNode(children, metaData), input)
}

fun value(input: TreeNode): Int {
    return if (input.children.isEmpty()) {
        input.metaData.sum()
    } else {
        input.metaData.map{ idx ->
            input.children.getOrNull(idx-1)
        }.map {
            if (it == null) 0 else value(it)
        }.sum()
    }
}
