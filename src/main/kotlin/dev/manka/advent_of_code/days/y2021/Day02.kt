package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day


private enum class Direction {
    Forward, Down, Up;
    companion object {
        fun getByLowerCaseName(name: String): Direction {
            return values().first { it.name.lowercase() == name }
        }
    }
}

class Day02 : Day(2, "Dive") {
    override fun partOne(): String {
        var horizontal = 0
        var depth = 0
        inputAsDirectionPairs()
            .forEach {
                when (it.first) {
                    Direction.Forward -> horizontal += it.second
                    Direction.Down -> depth += it.second
                    Direction.Up -> depth -= it.second
                }
            }

        return "horizontal $horizontal vertical $depth multiplication ${horizontal * depth}"
    }

    override fun partTwo(): String {
        var horizontal = 0
        var aim = 0
        var depth = 0
        inputAsDirectionPairs()
            .forEach {
                when (it.first) {
                    Direction.Forward -> {
                        horizontal += it.second
                        depth += aim * it.second
                    }
                    Direction.Down -> aim += it.second
                    Direction.Up -> aim -= it.second
                }
            }

        return "horizontal $horizontal vertical $depth multiplication ${horizontal * depth}"
    }

    private fun inputAsDirectionPairs() = inputList.asSequence().map { toPair(it) }

    private fun toPair(line: String): Pair<Direction, Int> {
        val values = line.split(" ")
        return Pair(Direction.getByLowerCaseName(values[0]), values[1].toInt())
    }

}