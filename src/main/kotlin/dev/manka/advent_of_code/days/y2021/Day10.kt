package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day
import java.util.*

class Day10 : Day(10, "Syntax Scoring") {

    override fun partOne(): String {
        return inputList.map { it.toCharArray() }.sumOf { calculateScoreForErrors(it) }.toString()
    }


    override fun partTwo(): String {
        val scoresOfIncomplete = inputList.map { it.toCharArray() }
            .filter { calculateScoreForErrors(it) == 0 }
            .map { calculateScoreForIncomplete(it) }
            .sorted()
        return scoresOfIncomplete[scoresOfIncomplete.size / 2].toString()
    }

    private fun calculateScoreForIncomplete(chars: CharArray): Long {
        var index = 0
        val brackets = LinkedList<Char>()
        while (index < chars.size) {
            val c = chars[index]
            index++
            when (c) {
                '(', '{', '<', '[' -> brackets.addLast(c)
                else -> {
                    brackets.removeLastOrNull()
                }
            }
        }
        val reduce = brackets.map {
            when (it) {
                '(' -> 1L
                '[' -> 2L
                '{' -> 3L
                '<' -> 4L
                else -> 0L
            }
        }.reversed().reduce { acc, i -> acc * 5 + i }
        return reduce

    }

    private fun calculateScoreForErrors(chars: CharArray): Int {
        var index = 0
        val brackets = LinkedList<Char>()
        while (index < chars.size) {
            val c = chars[index]
            index++
            when (c) {
                '(', '{', '<', '[' -> brackets.addLast(c)
                ')' -> if (brackets.removeLastOrNull() != '(') return 3
                ']' -> if (brackets.removeLastOrNull() != '[') return 57
                '}' -> if (brackets.removeLastOrNull() != '{') return 1197
                '>' -> if (brackets.removeLastOrNull() != '<') return 25137
            }
        }
        return 0
    }

}