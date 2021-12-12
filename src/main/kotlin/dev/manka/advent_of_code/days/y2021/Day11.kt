package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day11 : Day(11, "Dumbo Octopus") {
    override fun partOne(): String {
        val octopuses = Octopuses(inputList.map { it.split("").filter { c -> c.isNotBlank() }.map { c -> c.toInt() } })
        repeat(100) {
            octopuses.simulateStep()
        }

        return "${octopuses.flashes}"
    }


    override fun partTwo(): String {
        val octopuses = Octopuses(inputList.map { it.split("").filter { c -> c.isNotBlank() }.map { c -> c.toInt() } })
        var steps = 0
        while(!octopuses.allHaveZero()) {
            octopuses.simulateStep()
            steps++
        }
        return "$steps"
    }

    private class Octopuses(octopuses: List<List<Int>>) {
        val matrix = octopuses.map { it.toMutableList() }
        var flashes = 0L
        fun simulateStep() {
            matrix.forEachIndexed { x, row -> row.forEachIndexed { y, _ -> matrix[x][y]++ } }
            matrix.forEachIndexed { x, row ->
                row.forEachIndexed { y, i ->
                    if (i == 10) {
                        flash(x,y)
                    }
                }
            }
            matrix.forEachIndexed{ x, row -> row.forEachIndexed { y, i -> if (i == -1) matrix[x][y] = 0 } }
        }


        private fun flash(x: Int, y: Int) {
            flashes++
            matrix[x][y]=-1
            for (x2 in (x - 1).coerceAtLeast(0)..(x + 1).coerceAtMost(matrix.size-1)) {
                for (y2 in (y - 1).coerceAtLeast(0)..(y + 1).coerceAtMost(matrix.first().size-1)) {
                    if(matrix[x2][y2]!=-1){
                        matrix[x2][y2]++
                        if( matrix[x2][y2]>=10){
                            flash(x2,y2)
                        }
                    }
                }
            }
        }

        fun allHaveZero() : Boolean{
            return matrix.all { it.all { i -> i == 0 } }
        }

        override fun toString(): String {
            return "octopuses:\n${matrix.map { "$it\n" }} , flash $flashes"
        }
    }
}