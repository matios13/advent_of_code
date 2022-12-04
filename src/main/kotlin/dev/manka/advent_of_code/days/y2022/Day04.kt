package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day04 : Day(4, "Camp Cleanup", 2022) {
    override fun partOne(): String {
        return getPairs(inputList).count { isOneRangeInsideOther(it.first, it.second) }.toString()
    }

    override fun partTwo(): String {
        return getPairs(inputList).count { areRangesOverlapping(it.first, it.second) }.toString()
    }


    private fun getPairs(pairs: List<String>): List<Pair<Pair<Int,Int>, Pair<Int,Int>>> =
        pairs
            .map {it.split(",")}
            .map { getSectionsForElf(it[0]) to getSectionsForElf(it[1]) }

    private fun getSectionsForElf(string: String): Pair<Int, Int> {
        val sections = string.split("-")
        return sections[0].toInt() to sections[1].toInt()
    }

    private fun areRangesOverlapping(range1: Pair<Int, Int>, range2: Pair<Int, Int>): Boolean {
        return (range1.first in range2.first..range2.second || range1.second in range2.first..range2.second) ||
                (range2.first in range1.first..range1.second || range2.second in range1.first..range1.second)
    }
    private fun isOneRangeInsideOther(range1: Pair<Int, Int>, range2: Pair<Int, Int>): Boolean {
        return (range1.first in range2.first..range2.second && range1.second in range2.first..range2.second)||
                (range2.first in range1.first..range1.second && range2.second in range1.first..range1.second)
    }

}