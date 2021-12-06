package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day06 : Day(6, "Lanternfish") {
    override fun partOne(): String {
        val numberOfFish = calculateFish(80)
        return "$numberOfFish"
    }

    override fun partTwo(): String {
        val numberOfFish = calculateFish(256)
        return "$numberOfFish"
    }

    private fun calculateFish(nrOfDays: Int): Long {
        var fishGroup =
            inputList.first().split(",").groupingBy { it.toInt() }.eachCount().map { it.key to it.value.toLong() }
                .toMap()
        for (i in 1..nrOfDays) {
            val nextGen =
                fishGroup.asSequence().filter { it.key != 0 }.map { it.key - 1 to it.value }.toMap().toMutableMap()
            nextGen[8] = fishGroup.getOrDefault(0, 0)
            nextGen.compute(6) { _, v ->
                val oldFish = fishGroup.getOrDefault(0, 0)
                if (v != null) {
                    v + oldFish
                } else {
                    oldFish
                }
            }
            fishGroup = nextGen
        }
        return fishGroup.map { it.value }.sum()
    }

}
