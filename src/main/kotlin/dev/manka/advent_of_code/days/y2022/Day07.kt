package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day07 : Day(7, "Tuning Trouble ", 2022) {
    override fun partOne(): String {
        return parseInput(inputList)
            .getAllDirectoriesAndSubDirectoriesWithTotalSizeSmallerOrEqualThan(100000)
            .sumOf { it.totalSize() }.toString()
    }

    override fun partTwo(): String {
        val root = parseInput(inputList)
        val minSizeToFind = 30000000 - (70000000 - root.totalSize())
        return root.getAllDirectoriesAndSubDirectoriesWithTotalSizeBiggerOrEqualThan(minSizeToFind)
            .minBy { it.totalSize() }.totalSize().toString()
    }

    private fun parseInput(inputList: List<String>) :File {
        val root = File("/", 0)
        var currentDirectory = root
        inputList.forEach {
            if (it.startsWith("$")) {
                currentDirectory = handleCommand(it, currentDirectory)
            } else if (it.startsWith("dir")) {
                currentDirectory.addChildren(File(it.substring(4), 0, currentDirectory))
            } else{
                val sizeAndName = it.split(" ")
                currentDirectory.addChildren(File(sizeAndName[1], sizeAndName[0].toInt(), currentDirectory))
            }
        }
        return root
    }

    private fun handleCommand(it: String, currentDirectory: File) :File{
        val splited = it.split(" ")
        if (splited[1] == "cd") {
            if (splited[2] == "..") {
                return currentDirectory.parent!!
            } else if(splited[2] != "/"){
                return currentDirectory.children.first { it.name == splited[2] }
            }
        }
        return currentDirectory
    }

    private data class File(
        val name: String,
        val size: Int,
        val parent: File? = null,
        val children: MutableList<File> = ArrayList()
    ) {
        fun totalSize(): Int = size + children.sumOf { it.totalSize() }
        private fun getAllDirectoriesAndSubDirectories(): List<File> = children.filter { it.size==0 } + children.flatMap { it.getAllDirectoriesAndSubDirectories() }
        fun getAllDirectoriesAndSubDirectoriesWithTotalSizeSmallerOrEqualThan(size: Int): List<File> =
            getAllDirectoriesAndSubDirectories().filter { it.totalSize() <= size }
        fun getAllDirectoriesAndSubDirectoriesWithTotalSizeBiggerOrEqualThan(size: Int): List<File> =
            getAllDirectoriesAndSubDirectories().filter { it.totalSize() >= size }
        fun addChildren(child: File) {
            children += child
        }

        override fun toString(): String {
            return "File(name='$name', size=$size, children=${children.size}),totalSize=${totalSize()}"
        }
        fun toStringWithTabs(nrOfSpaces:Int=0): String {
            val spaces = " ".repeat(nrOfSpaces)
            return "$spaces$name (${totalSize()}): ${if(children.size>0)"\n" else ""}${children.joinToString("\n") { it.toStringWithTabs(nrOfSpaces+2) }}"
        }

    }
}