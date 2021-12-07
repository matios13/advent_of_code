package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day
import java.util.stream.LongStream
import kotlin.math.abs

class Day07 : Day(7, "The Treachery of Whales") {
    override fun partOne(): String {
        val crabs = inputList.first().split(",").map { it.toLong() }
        var min = Long.MAX_VALUE
        for (i in 0..crabs.maxOrNull()!!) {
            val cost = crabs.sumOf { abs(it - i) }
            min = if (cost < min) cost else min
        }
        return "$min"
    }

    override fun partTwo(): String {
        val crabs = inputList.first().split(",").map { it.toLong() }
        var min = Long.MAX_VALUE
        for (i in 0..crabs.maxOrNull()!!) {
            val cost = crabs.sumOf { calculateCost(i, it) }
            min = if (cost < min) cost else min
        }
        return "$min"
    }


    private fun calculateCost(position1: Long, position2: Long): Long =
        LongStream.rangeClosed(1, abs(position1 - position2)).sum()

}
