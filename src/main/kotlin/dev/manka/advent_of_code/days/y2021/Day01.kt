package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day01 : Day(1,"Sonar Sweep"){
    override fun partOne(): String {
        return inputIntSequence()
            .countIncreses()
            .toString()
    }

    override fun partTwo(): String {
        return inputIntSequence()
            .windowed(3)
            .map{it.sum()}
            .countIncreses()
            .toString()
    }
    private fun Sequence<Int>.countIncreses() : Int{
        return this.zipWithNext()
            .count{it.second-it.first > 0}
    }

}