package dev.manka.advent_of_code.days.y2021

import arrow.core.*
import dev.manka.advent_of_code.days.Day

class Day18 : Day(18, "Snailfish") {

    override fun partOne(): String {
        var sumOfSnailfish = fromString(inputList.first())
        inputList.drop(1).forEach { sumOfSnailfish = sumOfSnailfish.add(fromString(it)) }
        return sumOfSnailfish.getMagnitude().toString()
    }

    override fun partTwo(): String {
        return inputList
            .mapIndexed { i, first ->
                inputList.filterIndexed { i2, _ -> i2 != i }.map { second -> fromString(first).add(fromString(second)) }
            }
            .flatten()
            .maxOf { it.getMagnitude() }
            .toString()
    }

    private fun fromString(i: String): Snailfish {
        val input = i.drop(1).dropLast(1)
        val sailfish = if (input.toIntOrNull() != null) {
            input.toInt().left()
        } else {
            val first: Snailfish
            val remaining =
                if (input.first().digitToIntOrNull() != null) {
                    first = Snailfish(input.first().digitToInt().left())
                    input.drop(2)
                } else {
                    val firstString = StringBuilder()
                    var brackets = 0
                    input.takeWhile {
                        if (it == '[') brackets++
                        if (it == ']') brackets--
                        firstString.append(it)
                        brackets != 0
                    }
                    first = fromString(firstString.toString())
                    input.substringAfter(firstString.toString()).drop(1)
                }
            val second =
                if (remaining.first().digitToIntOrNull() != null) {
                    Snailfish(remaining.first().digitToInt().left())
                } else {
                    val secondString = StringBuilder()
                    var brackets = 0
                    remaining.takeWhile {
                        if (it == '[') brackets++
                        if (it == ']') brackets--
                        secondString.append(it)
                        brackets != 0
                    }
                    fromString(secondString.toString())
                }
            (first to second).right()
        }
        return Snailfish(sailfish)
    }

    class Snailfish(private var snailfish: Either<Int, Pair<Snailfish, Snailfish>>) {


        private fun reduceExplodes(depth: Int): Pair<Boolean, Pair<Int?, Int?>?> {

            return snailfish.map {
                if (depth == 4) {
                    val left = it.first.snailfish.swap()
                        .getOrElse { throw Error("we are fucked for $snailfish in ${it.first}") }
                    val right = it.second.snailfish.swap()
                        .getOrElse { throw Error("we are fucked for $snailfish in ${it.second}") }
                    snailfish = 0.left()
                    true to (left to right)
                } else {
                    var reduceExplodes = it.first.reduceExplodes(depth + 1)
                    if (reduceExplodes.first && reduceExplodes.second != null) {
                        val reducedSecond = reduceExplodes.second?.second?.let { num ->
                            if (it.second.addLeft(num))
                                reduceExplodes.second?.first to null
                            else
                                reduceExplodes.second
                        } ?: reduceExplodes.second
                        true to reducedSecond
                    } else {
                        if (!reduceExplodes.first) {
                            reduceExplodes = it.second.reduceExplodes(depth + 1)
                        }
                        if (reduceExplodes.first && reduceExplodes.second != null) {
                            val reducedSecond = reduceExplodes.second?.first?.let { num ->
                                if (it.first.addRight(num))
                                    null to reduceExplodes.second?.second
                                else
                                    reduceExplodes.second
                            } ?: reduceExplodes.second
                            true to reducedSecond
                        } else {
                            reduceExplodes
                        }
                    }
                }
            }.getOrElse { false to null }
        }

        fun getMagnitude(): Int {
            return snailfish.map { 3 * it.first.getMagnitude() + 2 * it.second.getMagnitude() }.getOrHandle { it }
        }

        private fun reduceBig(): Boolean {
            return snailfish.map { it.first.reduceBig() || it.second.reduceBig() }.getOrHandle {
                if (it > 9) {
                    val values = if (it % 2 == 0) {
                        (it / 2) to (it / 2)
                    } else {
                        (it / 2) to (it / 2) + 1
                    }
                    snailfish = (Snailfish(values.first.left()) to Snailfish(values.second.left())).right()
                    true
                } else
                    false
            }
        }

        private fun addRight(number: Int): Boolean =
            snailfish.map { it.second.addRight(number) }.getOrHandle { snailfish = (it + number).left(); true }


        override fun toString(): String {
            return snailfish.map { "[${it.first},${it.second}]" }.getOrHandle { it.toString() }
        }

        private fun addLeft(number: Int): Boolean =
            snailfish.map { it.first.addLeft(number) }.getOrHandle { snailfish = (it + number).left(); true }

        fun add(secondSnailfish: Snailfish): Snailfish {
            val pair: Pair<Snailfish, Snailfish> = this to secondSnailfish
            val newSnailfish = Snailfish(pair.right())
            do {
                val wasReduced = newSnailfish.reduceExplodes(0).first || newSnailfish.reduceBig()
            } while (wasReduced)
            return newSnailfish
        }
    }

}