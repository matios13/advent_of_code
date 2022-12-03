package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day02 : Day(2, "Rock Paper Scissors", 2022) {
    override fun partOne(): String {
        return extractRoundsWithStrategy1(inputList).sumOf { calculateRound(it.first, it.second) }.toString()
    }

    override fun partTwo(): String {
        return extractRoundsWithStrategy2(inputList).sumOf { calculateRound(it.first, it.second) }.toString()
    }

    private fun calculateRound(opponent: RPS, you: RPS): Int {
        return when (you) {
            RPS.ROCK -> 1
            RPS.PAPER -> 2
            else -> 3
        } + calculateWinOrLose(opponent, you)
    }

    private fun calculateWinOrLose(opponent: RPS, you: RPS): Int {
        return when {
            getWinForShape(opponent) == you -> 6
            opponent == you -> 3
            else -> 0
        }
    }

    private fun extractRoundsWithStrategy1(rounds: List<String>): List<Pair<RPS, RPS>> = rounds
        .map { it.split(" ").let { s -> fromStringToRPS(s[0]) to fromStringToRPS(s[1]) } }

    private fun extractRoundsWithStrategy2(rounds: List<String>): List<Pair<RPS, RPS>> = rounds
        .map { it.split(" ").let { s -> fromStringToPairOfRPS(s[0], s[1]) } }


    private fun fromStringToRPS(rps: String): RPS {
        return when (rps) {
            "A" -> RPS.ROCK
            "B" -> RPS.PAPER
            "C" -> RPS.SCISSORS
            "X" -> RPS.ROCK
            "Y" -> RPS.PAPER
            "Z" -> RPS.SCISSORS
            else -> throw IllegalArgumentException("Invalid RPS")
        }
    }

    private fun fromStringToPairOfRPS(opponent: String, you: String): Pair<RPS, RPS> {
        val opponentShape = fromStringToRPS(opponent)
        val yourShape = when (you) {
            "X" -> getLossForShape(opponentShape)
            "Y" -> opponentShape
            "Z" -> getWinForShape(opponentShape)
            else -> throw IllegalArgumentException("Invalid RPS")
        }
        return opponentShape to yourShape
    }

    private fun getWinForShape(shape: RPS): RPS {
        return when (shape) {
            RPS.ROCK -> RPS.PAPER
            RPS.PAPER -> RPS.SCISSORS
            RPS.SCISSORS -> RPS.ROCK
        }
    }

    private fun getLossForShape(shape: RPS): RPS {
        return when (shape) {
            RPS.ROCK -> RPS.SCISSORS
            RPS.PAPER -> RPS.ROCK
            RPS.SCISSORS -> RPS.PAPER
        }
    }

    private enum class RPS {
        ROCK, PAPER, SCISSORS
    }
}