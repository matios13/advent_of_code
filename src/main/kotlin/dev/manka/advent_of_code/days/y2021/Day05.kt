package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day05 : Day(5, "Hydrothermal Adventure") {
    override fun partOne(): String {
        val pipes = toCoordinates()
        val sum = getOverlappingLines(pipes, false)
        return "$sum"
    }

    override fun partTwo(): String {
        val pipes = toCoordinates()
        val sum = getOverlappingLines(pipes, true)
        return "$sum"
    }

    private fun getOverlappingLines(pipes: List<List<Pair<Int, Int>>>, diagonal: Boolean): Int {
        val allPoints = List(1000) { IntArray(1000).toMutableList() }
        pipes.forEach {
            if (it[0].first == it[1].first) {
                for (i in it[0].second.coerceAtMost(it[1].second)..it[0].second.coerceAtLeast(it[1].second)) {
                    allPoints[it[0].first][i] += 1
                }
            } else if (it[0].second == it[1].second) {
                for (i in it[0].first.coerceAtMost(it[1].first)..it[0].first.coerceAtLeast(it[1].first)) {
                    allPoints[i][it[0].second] += 1
                }
            } else if (diagonal) {
                var x = it[0].first
                var y = it[0].second
                do {
                    allPoints[x][y] += 1
                    x = if (x > it[1].first) x - 1 else x + 1
                    y = if (y > it[1].second) y - 1 else y + 1
                } while (x != it[1].first)
                allPoints[x][y] += 1
            }
        }
        return allPoints.sumOf { it.count { i -> i > 1 } }
    }

    private fun toCoordinates() =
        inputList.map {
            it.split(" -> ").map { coordinates ->
                coordinates.substringBefore(",").toInt() to coordinates.substringAfter(",").toInt()
            }
        }


}