package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day
import java.util.*

class Day15 : Day(15, "") {


    override fun partOne(): String {
        val risks = inputList.map { it.toCharArray().map { c -> c.digitToInt() } }
        return findRoutes(risks).toString()
    }

    override fun partTwo(): String {
        val risks = inputList.map { it.toCharArray().map { c -> c.digitToInt() } }
        val risksExtendedHorizontally = risks.map {
            (0 until 5).map { i ->
                it.map { v -> v.addWithWrap(i) }
            }.flatten()
        }
        val extendedRisks = mutableListOf<List<Int>>()
        (0 until 5).map { i ->
            risksExtendedHorizontally.forEach { extendedRisks.add(it.map { v -> v.addWithWrap(i) }) }
        }
        return findRoutes(extendedRisks).toString()
    }

    private fun Int.addWithWrap(i: Int): Int = if ((this + i) > 9) this + i - 9 else this + i


    private fun findRoutes(risks: List<List<Int>>): Int {
        val totalRisks = risks.map { IntArray(risks.first().size) }
        val nextPoints = LinkedList(listOf(0 to 0))

        while (nextPoints.isNotEmpty()) {
            val position = nextPoints.remove()
            val risk = totalRisks[position.first][position.second]
            val lowRiskMovements = possibleMovements(position, risks).filter { p ->
                val candidateRisk = risk + risks[p.first][p.second]
                val previousRisk = totalRisks[p.first][p.second]
                if (previousRisk == 0 || candidateRisk < previousRisk) {
                    totalRisks[p.first][p.second] = candidateRisk
                    true
                } else false
            }
            nextPoints.addAll(lowRiskMovements)
        }

        return totalRisks[risks.size - 1][risks.first().size - 1]
    }

    private fun possibleMovements(pos: Pair<Int, Int>, risks: List<List<Int>>): Set<Pair<Int, Int>> {
        val possiblePositions = mutableListOf<Pair<Int, Int>>()
        if (pos.first != risks.size - 1) possiblePositions.add((pos.first + 1 to pos.second))
        if (pos.second != risks.first().size - 1) possiblePositions.add((pos.first to pos.second + 1))
        if (pos.first != 0) possiblePositions.add((pos.first - 1 to pos.second))
        if (pos.second != 0) possiblePositions.add((pos.first to pos.second - 1))
        return possiblePositions.toSet()
    }

}


