package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day20 : Day(20, "Grove Positioning System", 2022) {
    override fun partOne(): String {
        val decrypted = inputList.map { it.toLong() }
            .withIndex()
            .decrypt()
        return sumOfImportantValues(decrypted)
    }
    override fun partTwo(): String {
        val decrypted = inputList.map { it.toLong() * 811589153 }
            .withIndex()
            .decrypt(10)
        return sumOfImportantValues(decrypted)
    }

    private fun sumOfImportantValues(decrypted: List<Long>): String {
        val indexZero = decrypted.indexOfFirst { it == 0L }
        return listOf(1000, 2000, 3000).sumOf { decrypted[(indexZero + it).mod(decrypted.size)] }.toString()
    }

    private fun Iterable<IndexedValue<Long>>.decrypt(nrOfTimes: Int = 1): List<Long> {
        val decrypted = toMutableList()
        repeat(nrOfTimes) {
            forEach {
                val position = decrypted.indexOf(it)
                val newPosition = (position + it.value).mod(decrypted.size - 1)
                decrypted.removeAt(position)
                decrypted.add(newPosition, it)
            }
        }
        return decrypted.map { it.value }
    }

}


