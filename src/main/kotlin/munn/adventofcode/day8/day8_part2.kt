package munn.adventofcode.day8

import java.io.File

fun main(args: Array<String>) {

    val data = File(args.first()).readText().split(" ").map { it.toInt() }
    var index = 0
    fun parseNode(): Node = Node(List(data[index++]) { parseNode() }, List(data[index++]) { data[index++] })
    val rootNode = parseNode()

    fun nodeValue(node: Node): Int = if (node.children.isEmpty())
        node.metaData.sum()
    else
        node.metaData.mapNotNull { i -> node.children.getOrNull(i - 1) }.sumBy { nodeValue(it) }

    println(nodeValue(rootNode))
}