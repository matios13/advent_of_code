package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import kotlin.math.pow

class Day25 :Day(25, "Full of Hot Air", 2022) {
    override fun partOne(): String {
        val sumOfNumbers = inputList.sumOf { toDecimalNumber(it) }
        return toSnafu(sumOfNumbers)
    }

    private fun toDecimalNumber(snafu: String) : Long {
        return snafu.map { when(it){
            '2' -> 2
            '1' -> 1
            '-' -> -1
            '=' -> -2
            else -> 0
        } }.reversed()
            .foldIndexed(0L){ index, acc, digit -> acc + (digit * (5.0.pow(index).toLong())) }
    }
    private fun toSnafu(number: Long) : String {
        var caryOver= 0
        return  number.toString(5).map{it.digitToInt()}.reversed().fold("") { acc, digit ->
            val sum = digit + caryOver
            caryOver = sum / 3
            when (sum % 5) {
                4 -> "-"
                3 -> "="
                2 -> "2"
                1 -> "1"
                0 -> "0"
                else -> throw IllegalStateException("Should not happen")
            } + acc
        }
    }

    override fun partTwo(): String {
        return "Not implemented yet"
    }


}