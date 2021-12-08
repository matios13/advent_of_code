package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day08 : Day(8, "Seven Segment Search ") {

    override fun partOne(): String {
        val numberOfEasyOutputValues = inputList.asSequence()
            .map { it.split("|")[1] }
            .map {
                it.split(" ").count { value ->
                    val length = value.trim().length
                    length == 2 || length == 3 || length == 4 || length == 7
                }
            }.sum()
        return "$numberOfEasyOutputValues"
    }

    override fun partTwo(): String {
        val o = inputList.asSequence()
            .map { it.split("|") }
            .map {
                val values =
                    it[0].split(" ").map { v -> v.trim() }.filterNot { v -> v.isBlank() }
                        .map { v -> v.toCharArray().toList().sorted() }
                val outputValues =
                    it[1].split(" ").map { v -> v.trim() }.filterNot { v -> v.isBlank() }
                        .map { v -> v.toCharArray().toList().sorted() }
                SingleLine(values, outputValues)
            }
            .map { it.figureOutOutputValues() }.sum()
        return "$o"
    }


    private data class SingleLine(val values: List<List<Char>>, val outputValues: List<List<Char>>) {
        private enum class Digit(val segments: List<Char>) {
            ZERO("abcefg".toCharArray().toList()),
            ONE("cf".toCharArray().toList()),
            TWO("acdeg".toCharArray().toList()),
            THREE("acdfg".toCharArray().toList()),
            FOUR("bcdf".toCharArray().toList()),
            FIVE("abdfg".toCharArray().toList()),
            SIX("abdefg".toCharArray().toList()),
            SEVEN("acf".toCharArray().toList()),
            EIGHT("abcdefg".toCharArray().toList()),
            NINE("abcdfg".toCharArray().toList());

            companion object {
                fun getDigitsWithNumberOfSegments(n: Int): List<Digit> = values().filter { it.segments.size == n }
            }
        }

        private val possibleValues = 'a'..'g'
        val possibleValuesPerSegment = possibleValues.associateWith { possibleValues.toMutableSet() }

        fun figureOutOutputValues(): Int {
            val map = resolvePatternDigits()
            return toInt(outputValues.map { map.getValue(it) })
        }

        private fun toInt(t: List<Int>): Int {
            return t.joinToString("").toInt()
        }

        private fun resolvePatternDigits(): Map<List<Char>, Int> {
            values.forEach { pattern ->
                removeSameRetainDistinctWithCharacterIn(pattern)
                removeSingleValues()
            }
            return values.map { it.sorted() }
                .associateWith { p -> p.map { possibleValuesPerSegment[it]!!.single() }.toDigit().ordinal }
        }

        private fun removeSameRetainDistinctWithCharacterIn(
            pattern: List<Char>,
        ) {
            val possibleDigits = Digit.getDigitsWithNumberOfSegments(pattern.size)
            val distinctSegments = getDistinctSegments(possibleDigits)
            val sameSegments = getSameSegments(possibleDigits)
            possibleValues.forEach {
                val possibleSegments = possibleValuesPerSegment[it]!!
                if (it in pattern) {
                    possibleSegments.retainAll(distinctSegments)
                } else {
                    possibleSegments.removeAll(sameSegments)
                }
            }
        }

        private fun removeSingleValues() {
            possibleValues.forEach {
                val possibleSegments = possibleValuesPerSegment[it]!!
                if (possibleSegments.size == 1) {
                    possibleValues.filter { c -> c != it }
                        .forEach { c -> possibleValuesPerSegment[c]?.removeAll(possibleSegments) }
                }
            }
        }

        private fun getSameSegments(possibleDigits: List<Digit>) =
            possibleDigits.map { it.segments.toMutableSet() }.reduce { acc, it -> acc.apply { retainAll(it) } }

        private fun getDistinctSegments(possibleDigits: List<Digit>) =
            possibleDigits.map { it.segments }.flatten().distinct().toSet()


        private fun List<Char>.toDigit(): Digit = Digit.values().first { d -> d.segments == this.sorted() }


    }
}

