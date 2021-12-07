package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day


class Day03 : Day(3, "Binary diagnostic") {


    override fun partOne(): String {
        var (gamma, epsilon) = listOf("", "")

        inputList.first().toCharArray().forEachIndexed { i, _ ->
            gamma += if (isOneMostCommonBit(inputList, i)) '0' else '1'
            epsilon += if (isOneMostCommonBit(inputList, i)) '1' else '0'
        }

        return "${Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)}"
    }


    override fun partTwo(): String {
        var oxygen = inputList
        var co2 = inputList
        for (i in 0 until inputList[0].length) {
            if (oxygen.size != 1) {
                oxygen = filter(oxygen, i, true)
            }
            if (co2.size != 1) {
                co2 = filter(co2, i, false)
            }
        }

        return "" + (oxygen[0].toInt(2) * co2[0].toInt(2))
    }

    private fun filter(input: List<String>, position: Int, oxygen: Boolean = true): List<String> {
        val needed = if (generateList(input)[position]) {
            if (oxygen) '1' else '0'
        } else {
            if (oxygen) '0' else '1'
        }
        return input.filter { it[position] == needed }
    }

    private fun generateList(input: List<String>): List<Boolean> = input.first().toCharArray().mapIndexed { i, _ ->
        isOneMostCommonBit(input, i)
    }

    private fun isOneMostCommonBit(list: List<String>, index: Int): Boolean =
        list.count { it.toCharArray()[index] == '1' }.toDouble() / list.size >= 0.5

}