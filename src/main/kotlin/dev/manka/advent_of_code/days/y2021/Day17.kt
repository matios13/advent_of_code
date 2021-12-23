package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day17 : Day(17, "Trick Shot") {

    override fun partOne(): String {
        val targetArea = TargetArea(inputList.first())
        return (0..targetArea.x.last).maxOf { x ->
            (targetArea.y.first..targetArea.y.first.absoluteValue)
                .map { y -> trajectory((x to y)).takeWhile { !targetArea.hasOvershot(it) } }
                .filter { it.any { p -> targetArea.has(p) } }
                .maxOfOrNull { it.maxOf { p -> p.second } } ?: 0
        }.toString()
    }

    override fun partTwo(): String {
        val targetArea = TargetArea(inputList.first())
        return (0..targetArea.x.last).sumOf { x ->
            (targetArea.y.first..targetArea.y.first.absoluteValue)
                .map { y -> trajectory(x to y).first { targetArea.hasOvershot(it) || targetArea.has(it) } }
                .count { targetArea.has(it) }
        }.toString()
    }

    private fun trajectory(velocity: Pair<Int, Int>): Sequence<Pair<Int, Int>> = sequence {
        var x = 0
        var y = 0
        var Vx = velocity.first
        var Vy = velocity.second
        while (true) {
            x += Vx
            y += Vy
            Vx += if (Vx == 0) 0 else Vx.sign * -1
            Vy -= 1
            yield(x to y)
        }
    }

    private class TargetArea(input: String) {
        val x: IntRange = input.substringAfter("=").substringBefore(".").toInt()..
                input.substringAfter("..").substringBefore(",").toInt()
        val y: IntRange = input.substringAfterLast("=").substringBefore(".").toInt()..
                input.substringAfterLast(".").toInt()

        fun has(point: Pair<Int, Int>): Boolean = point.first in x && point.second in y

        fun hasOvershot(point: Pair<Int, Int>): Boolean = point.first > x.last || point.second < y.first
    }


}