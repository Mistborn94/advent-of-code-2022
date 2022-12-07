package day7

import java.util.*

//Find all the directories with a total size of at most 100000.
// What is the sum of the total sizes of those directories?
fun solveA(text: String): Int {
    val (directories, subDirectories) = navigateFileSystem(text)

    return directories.map { (key, _) -> totalSize(key, directories, subDirectories) }
        .filter { it <= 100000 }.sum()
}

fun totalSize(dir: String, directories: MutableMap<String, Int>, subDirectories: MutableMap<String, MutableList<String>>): Int {
    return directories[dir]!! + subDirectories[dir]!!.sumOf { totalSize(it, directories, subDirectories) }
}

fun solveB(text: String): Int {
    val (directories, subDirectories) = navigateFileSystem(text)

    val totalSize = totalSize("/", directories, subDirectories)
    val free = 70000000 - totalSize
    val needed = 30000000 - free
    return directories.map { (key, _) -> totalSize(key, directories, subDirectories) }
        .filter { it >= needed }.min()
}

private fun navigateFileSystem(text: String): Pair<MutableMap<String, Int>, MutableMap<String, MutableList<String>>> {
    val dirStack = Stack<String>()
    var dirPath = ""
    val directories = mutableMapOf<String, Int>()
    val subDirectories = mutableMapOf<String, MutableList<String>>()

    text.lines().forEach { line ->
        if (line.startsWith("\$ cd")) {
            if (line.endsWith("/")) {
                dirStack.clear()
                dirStack.push("/")
            } else if (line.endsWith("..")) {
                dirStack.pop()
            } else {
                dirStack.push(line.substring(5))
            }
            dirPath = dirStack.joinToString(separator = "/")
            directories.putIfAbsent(dirPath, 0)
            subDirectories.putIfAbsent(dirPath, mutableListOf())
        } else if (line.startsWith("\$ ls")) {
            //do nothing
        } else if (line.startsWith("dir")) {
            subDirectories[dirPath]!!.add("$dirPath/${line.substring(4)}")
        } else {
            val size = line.substringBefore(' ').toInt()
            directories[dirPath] = (directories[dirPath] ?: 0) + size
        }
    }
    return Pair(directories, subDirectories)
}
