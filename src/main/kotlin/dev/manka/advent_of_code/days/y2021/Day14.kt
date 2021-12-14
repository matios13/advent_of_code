package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day14 : Day(14, "Extended Polymerization") {
    override fun partOne(): String {

        var polymer = inputList.first()
        val rules = inputList.drop(2).map { it.split(" -> ") }.associate { it[0] to it[1] }
        repeat(10) {
            polymer = polymer[0].toString() + polymer.toCharArray().toList().windowed(2)
                .joinToString("") { "${rules["${it[0]}${it[1]}"]}${it[1]}" }

        }
        val map = polymer.toCharArray().toList().groupingBy { it }.eachCount()
        val score = (map.maxByOrNull { it.value }!!).value - map.minByOrNull { it.value }!!.value
        return "$score"
    }

    override fun partTwo(): String {

        var polymer = inputList.first().toCharArray().toList().windowed(2).map { "${it[0]}${it[1]}" }.groupingBy { it }
            .fold(0L) { acc, _ -> acc + 1 }

        val rules = inputList.drop(2).map { it.split(" -> ") }.associate { it[0] to it[1] }
        repeat(40) {
            polymer = polymer.map { e ->
                val newLetter = rules[e.key]!!
                val entry1 = e.key[0].toString() + newLetter to e.value
                val entry2 = newLetter + e.key[1].toString() to e.value
                listOf(entry1, entry2)
            }.flatten().groupingBy { p -> p.first }.fold(0L) { acc, e -> acc + e.second }
        }
        val map = polymer.map { listOf(it.key[0] to it.value, it.key[1] to it.value) }.flatten().groupingBy { it.first }
            .fold(0L) { acc, e ->
                acc + e.second
            }.toMutableMap()
        map.compute(inputList.first().first()) { _, v -> if (v != null) v + 1 else 1 }
        map.compute(inputList.first().last()) { _, v -> if (v != null) v + 1 else 1 }
        map.keys.forEach { map.computeIfPresent(it) { _, v -> v / 2 } }
        val score = (map.maxByOrNull { it.value }!!).value - map.minByOrNull { it.value }!!.value
        return "$score"
    }

}
