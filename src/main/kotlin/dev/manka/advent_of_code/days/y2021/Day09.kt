package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day09 : Day(9, "Smoke Basin") {

    override fun partOne(): String {
        val matrix = inputList.map { it.split("").filterNot { s -> s.isBlank() }.map { s -> s.toInt() }.toIntArray() }
        val minsCoordinates = matrix.mapIndexed { x, ints ->
            ints.mapIndexed { y, i ->
                val valuesInArea = emptyList<Int>().toMutableList()
                for (sx in 0.coerceAtLeast(x - 1)..(matrix.size - 1).coerceAtMost(x + 1)) {
                    for (sy in 0.coerceAtLeast(y - 1)..(ints.size - 1).coerceAtMost(y + 1)) {
                        valuesInArea.add(matrix[sx][sy])
                    }
                }
                if (valuesInArea.minOrNull() == i) x to y else null
            }.filterNotNull()
        }.flatten()
        val risk = minsCoordinates.sumOf { matrix[it.first][it.second] + 1 }
        return "$risk"
    }

    override fun partTwo(): String {
        val matrix = inputList.map { it.split("").filterNot { s -> s.isBlank() }.map { s -> s.toInt() }.toIntArray() }
        val basins = mutableListOf<Basin>()

        matrix.forEachIndexed { x, ints ->
            ints.forEachIndexed { y, i ->
                if (i < 9 && !basins.containsCoordinates(x, y)) {
                    basins += Basin(x, y, matrix)
                }
            }
        }
        basins.sortByDescending { it.coordinates.size }
        println(basins.map { it.coordinates.size })
        return "${basins.take(3).map { it.coordinates.size }.reduce { acc, i -> acc * i }}"
    }

    private data class Basin(val startingX: Int, val startingY: Int, val matrix: List<IntArray>) {
        val coordinates: MutableList<Pair<Int, Int>> = mutableListOf(startingX to startingY)

        init {
            calculateHorizontal(startingX to startingY)
        }

        private fun calculateHorizontal(pair: Pair<Int, Int>) {
            for (x in pair.first until matrix.size) {
                val i = matrix[x][pair.second]
                if (i == 9) break
                if (!coordinates.contains(x to pair.second)) {
                    coordinates += x to pair.second
                    calculateVertical(x to pair.second)
                }
            }
            for (x in pair.first downTo 0) {
                val i = matrix[x][pair.second]
                if (i == 9) break
                if (!coordinates.contains(x to pair.second)) {
                    coordinates += x to pair.second
                    calculateVertical(x to pair.second)
                }
            }
        }

        private fun calculateVertical(pair: Pair<Int, Int>) {
            for (y in pair.second until matrix.first().size) {
                val i = matrix[pair.first][y]
                if (i == 9) break
                if (!coordinates.contains(pair.first to y)) {
                    coordinates += pair.first to y
                    calculateHorizontal(pair.first to y)
                }
            }
            for (y in pair.second downTo 0) {
                val i = matrix[pair.first][y]
                if (i == 9) break
                if (!coordinates.contains(pair.first to y)) {
                    coordinates += pair.first to y
                    calculateHorizontal(pair.first to y)
                }
            }
        }
    }

    private fun MutableList<Basin>.containsCoordinates(x: Int, y: Int): Boolean {
        return this.firstOrNull { it.coordinates.contains(x to y) } != null
    }

}