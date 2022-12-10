package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day10 : Day(10, "Cathode-Ray Tube", 2022) {
    override fun partOne(): String {
        return cyclesWithValue
            .mapIndexed { cycle, value ->
                if ((cycle + 1) in listOf(20, 60, 100, 140, 180, 220)) {
                    (cycle + 1) * value
                } else {
                    0
                }
            }.sum().toString()
    }

    override fun partTwo(): String {
        return cyclesWithValue.chunked(40)
            .map {
                it.mapIndexed { index, i ->
                    if (index in (i - 1..i + 1)) "#" else "."
                }.joinToString("")
            }.joinToString("\n", "\n")
    }

    private val cyclesWithValue = sequence {
        var x = 1
        yield(x)
        inputList.forEach() {
            if (it == "noop") {
                yield(x)
            } else {
                yield(x)
                x += it.drop(5).toInt()
                yield(x)
            }
        }
    }
}