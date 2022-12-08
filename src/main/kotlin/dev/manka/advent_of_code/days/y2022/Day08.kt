package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day08 : Day(8, "Treetop Tree House", 2022) {
    override fun partOne(): String {
        return getVisibleTrees((inputList)).flatten().count { it.visible == true }.toString()
    }

    override fun partTwo(): String {
        return getScenicForTrees(inputList).flatten().maxOf { it.scenic }.toString()
    }

    private fun getVisibleTrees(input: List<String>): List<List<Tree>> {
        val trees = getTrees(input)
        trees.forEach { list -> list.forEach { tree -> tree.checkVisibility(trees) } }
        return trees
    }

    private fun getScenicForTrees(input: List<String>): List<List<Tree>> {
        val trees = getTrees(input)
        trees.forEach { list -> list.forEach { tree -> tree.calculateScenic(trees) } }
        return trees
    }

    private fun getTrees(input: List<String>): List<List<Tree>> {
        return input.map { it.toList() }
            .mapIndexed() { x, list -> list.mapIndexed() { y, h -> Tree(x, y, h.digitToInt()) } }
    }


    private data class Tree(val x: Int, val y: Int, val h: Int, var visible: Boolean? = null, var scenic: Int = 0) {
        fun checkVisibility(trees: List<List<Tree>>) {
            visible = (x == 0 || y == 0 || x == trees.size - 1 || y == trees.size - 1) ||
                    ((0 until x).all { trees[it][y].h < h }) ||
                    (((x + 1) until trees.size).all { trees[it][y].h < h }) ||
                    ((0 until y).all { trees[x][it].h < h }) ||
                    (((y + 1) until trees.size).all { trees[x][it].h < h })
        }

        fun calculateScenic(trees: List<List<Tree>>) {
            var left = ((x - 1) downTo 0).takeWhile { trees[it][y].h < h }.size
            var right = ((x + 1) until trees.size).takeWhile { trees[it][y].h < h }.size
            var up = ((y - 1) downTo 0).takeWhile { trees[x][it].h < h }.size
            var down = ((y + 1) until trees.size).takeWhile { trees[x][it].h < h }.size

            if ((trees.size - 1) - (x + 1) > right) right++
            if ((x - 1) > left) left++
            if (y - 1 > up) up++
            if ((trees.size - 1) - (y + 1) > down) down++

            scenic = left * right * up * down
        }
    }
}