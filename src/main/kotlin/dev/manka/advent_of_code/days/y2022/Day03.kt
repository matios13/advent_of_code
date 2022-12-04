package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day03 : Day(3, "Rucksack Reorganization", 2022) {
    override fun partOne(): String {
        val compartments = getCompartments(inputList)
        return compartments
            .map { it.first.toList() to it.second.toList() }
            .map { it.first.intersect(it.second.toSet()) }
            .sumOf {
                it.sumOfPriority()
            }.toString()
    }

    override fun partTwo(): String {
        return inputList
            .chunked(3)
            .map { it.map { s -> s.toList() } }
            .map { it[0].intersect(it[1]).intersect(it[2]) }
            .sumOf {
                it.sumOfPriority()
            }.toString()
    }


    private fun getCompartments(rucksacks: List<String>): List<Pair<String, String>> =
        rucksacks.map { it.substring(0, it.length / 2) to it.substring(it.length / 2, it.length) }

    private fun Set<Char>.sumOfPriority(): Int = sumOf { if (it.isUpperCase()) it.code - 38 else it.code - 96 }
}