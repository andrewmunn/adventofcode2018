package munn.adventofcode.day8

import java.io.File

fun main(args: Array<String>) {

    val data = File(args.first()).readText().split(" ").map { it.toInt() }
    var index = 0
    fun parseNode(): Node = Node(List(data[index++]) { parseNode() }, List(data[index++]) { data[index++] })
    val rootNode = parseNode()

    fun sumMetaData(node: Node): Int = node.metaData.sum() + node.children.map { sumMetaData(it) }.sum()

    println(sumMetaData(rootNode))
}

data class Node(val children: List<Node>, val metaData: List<Int>)