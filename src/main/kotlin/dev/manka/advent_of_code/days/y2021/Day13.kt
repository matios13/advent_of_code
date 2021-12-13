package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day13 : Day(13, "Transparent Origami") {
    override fun partOne(): String {
        val paper = Paper(inputList.asSequence()
            .filter { it.isNotBlank() }
            .filter { it.first() != 'f' })
        val folds = inputList.filter { it.startsWith("f") }
            .map { it.removePrefix("fold along ").split("=") }
            .map { it[0] to it[1].toInt() }
        paper.fold(folds.first().first, folds.first().second)
        return "${paper.getSize()}"
    }

    override fun partTwo(): String {
        val paper = Paper(inputList.asSequence()
            .filter { it.isNotBlank() }
            .filter { it.first() != 'f' })
        val folds = inputList.filter { it.startsWith("f") }
            .map { it.removePrefix("fold along ").split("=") }
            .map { it[0] to it[1].toInt() }
        folds.forEach { paper.fold(it.first, it.second) }
        return "$paper"
    }

    private class Paper(dots: Sequence<String>) {
        private val dotsPosition = dots.map { it.split(",") }
            .map { it[0].toInt() to it[1].toInt() }.toMutableList()

        fun getSize(): Int {
            return dotsPosition.size
        }

        fun fold(coordinates: String, value: Int) {
            if (coordinates == "y") {
                for (y in 0 until value) {
                    dotsPosition.filter { it.second == 2 * value - y }
                        .forEach {
                            if (!dotsPosition.contains(it.first to y)) {
                                dotsPosition.add(it.first to y)
                            }
                            dotsPosition.remove(it)
                        }
                }
            } else {
                for (x in 0 until value) {
                    dotsPosition.filter { it.first == 2 * value - x }
                        .forEach {
                            if (!dotsPosition.contains(x to it.second)) {
                                dotsPosition.add(x to it.second)
                            }
                            dotsPosition.remove(it)
                        }
                }
            }
        }

        override fun toString(): String {
            var str = "\n"
            for (y in 0..dotsPosition.maxByOrNull { it.second }!!.second) {
                for (x in 0..dotsPosition.maxByOrNull { it.first }!!.first) {
                    str += if (dotsPosition.contains(x to y)) "#" else "."
                }
                str += "\n"
            }
            str += "\n"
            return str
        }
    }

}
