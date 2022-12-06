package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import java.util.Deque
import java.util.LinkedList

class Day06 : Day(6, "Tuning Trouble ", 2022) {
    override fun partOne(): String {
        return calculateFirstMarker(4).toString()
    }

    override fun partTwo(): String {
        return calculateFirstMarker(14).toString()
    }

    private fun calculateFirstMarker(windowSize: Int) = (inputList[0].toList().windowed(windowSize).indexOfFirst { it.toSet().size == windowSize } + windowSize)

}