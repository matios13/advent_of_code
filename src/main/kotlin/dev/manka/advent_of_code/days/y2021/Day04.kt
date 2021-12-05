package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day


class Day04 : Day(4, "Binary diagnostic") {

    private class Board(val numbers: List<List<Int>>) {
        val marked: List<MutableList<Boolean>> = numbers.map { it.map { false }.toMutableList() }
        fun isWin(): Boolean {
            val horizontalFind = marked.firstOrNull { !it.contains(false) }

            return if (horizontalFind != null) true else {
                var verticalFind = false
                for (i in 0 until marked[0].size) {
                    verticalFind = !marked.map { it[i] }.contains(false)
                    if (verticalFind) {
                        break
                    }
                }
                return verticalFind
            }
        }

        fun markIfExist(number: Int) {
            val coordinates = numbers.mapIndexed { x, row ->
                val y = row.indexOf(number)
                if (y > -1) x to y else null
            }.filterNotNull().firstOrNull()
            if (coordinates != null) {
                marked[coordinates.first][coordinates.second] = true
            }
        }

        fun calculateScore(i: Int): Int {
            return numbers.mapIndexed { x, row -> row.mapIndexed { y, number -> if (marked[x][y]) 0 else number } }
                .flatten().sum() * i
        }
    }

    override fun partOne(): String {
        val numbers = inputList.first().split(",").map { it.toInt() }
        val allBoards = createBoards()

        var index = 0

        while (index < numbers.size) {
            allBoards.forEach { it.markIfExist(numbers[index]) }
            val winBoard = allBoards.firstOrNull { it.isWin() }
            if (winBoard != null) {
                return "${winBoard.calculateScore(numbers[index])}"
            }
            index++
        }
        return "nothing :( "
    }

    override fun partTwo(): String {
        val numbers = inputList.first().split(",").map { it.toInt() }
        val allBoards = createBoards()

        var index = -1
        while (allBoards.size > 1) {
            index++
            allBoards.forEach { it.markIfExist(numbers[index]) }
            allBoards.removeIf { it.isWin() }

        }

        val lastBoard = allBoards.first()
        while (!lastBoard.isWin()) {
            index++
            lastBoard.markIfExist(numbers[index])
        }

        return "${lastBoard.calculateScore(numbers[index])}"
    }

    private fun createBoards() = inputList.asSequence().drop(1)
        .filterNot { it.isBlank() }
        .chunked(5).map {
            val boardNumbers = it.asSequence()
                .map { b ->
                    b.split(" ")
                        .filterNot { value -> value.isBlank() }
                        .map { i -> i.toInt() }
                }
                .toList()
            Board(boardNumbers)
        }.toMutableList()
}
