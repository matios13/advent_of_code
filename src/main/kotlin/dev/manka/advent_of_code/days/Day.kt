package dev.manka.advent_of_code.days

import java.io.File
import java.io.FileNotFoundException

abstract class Day(val day: Int , val title : String) {
    protected val inputList: List<String> = getInputs()

    abstract fun partOne(): String

    abstract fun partTwo(): String

    protected fun inputIntSequence() : Sequence<Int> = inputList.asSequence().map { it.toInt()}

    private fun getInputs(): List<String> {
        val fileURL = javaClass.classLoader.getResource(
            "y2021/${day.toString().padStart(2, '0')}.txt"
        )
        if (fileURL != null) {
            return File(fileURL.toURI()).readLines()
        }
        throw FileNotFoundException("File for day $day was not found")
    }
}