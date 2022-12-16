package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import kotlin.math.pow

class Day16 : Day(16, "Proboscidea Volcanium", 2022) {
    override fun partOne(): String {
        val allValves = parseAllValves()
        return findMaxPressure(
            "AA",
            allValves.getAllNonZeroFlowRateValves().toSet(),
            30,
            0,
            allValves
        ).toString()
    }

    override fun partTwo(): String {
        val allValves = parseAllValves()
        val toVisitAll = allValves.getAllNonZeroFlowRateValves().toTypedArray()

        return (0 until 2.0.pow(toVisitAll.size).toInt()).toList().parallelStream()
            .map { getPermutationFor(toVisitAll, it) }
            .mapToInt {
                findMaxPressure("AA", it.first, 26, 0, allValves) + findMaxPressure(
                    "AA",
                    it.second,
                    26,
                    0,
                    allValves
                )
            }.max().orElse(-1).toString()
    }

    private fun parseAllValves(): Map<String, Valve> {
        val valves = inputList.map { Valve.of(it) }
        val scan = valves
            .associateBy { it.name }.let { scan ->
                val valves = scan.getAllNonZeroFlowRateValves().toSet()
                valves.plus("AA")
                    .map {
                        Valve(it, scan[it]!!.flowRate, findPaths(scan, it, valves.minus(it)))
                    }.associateBy { it.name }

            }
        return scan
    }

    private fun getPermutationFor(
        toVisitAll: Array<String>,
        it: Int
    ): Pair<MutableSet<String>, MutableSet<String>> {
        val mine = mutableSetOf<String>()
        val elephants = mutableSetOf<String>()
        toVisitAll.forEachIndexed { i, s ->
            if (it and (1 shl i) > 0) {
                elephants += s
            } else {
                mine += s
            }
        }
        return mine to elephants
    }

    private fun findPaths(valves: Map<String, Valve>, start: String, dest: Set<String>): Map<String, Int> {
        val visited = mutableSetOf(start)
        val toVisit = dest.minus(start).toMutableSet()

        val toExplore = ArrayDeque(listOf(start to 0))
        val result = mutableMapOf<String, Int>()
        if (dest.contains(start)) {
            result[start] = 0
        }
        while (toExplore.isNotEmpty() && toVisit.isNotEmpty()) {
            val (at, len) = toExplore.removeFirst()
            valves[at]!!.valves.forEach { (name, dist) ->
                val candidate = name to len + dist
                if (!visited.contains(name)) {
                    toExplore += candidate
                    visited += name
                    if (toVisit.contains(name)) {
                        result[name] = candidate.second
                        toVisit.remove(name)
                    }
                }
            }
        }
        return result
    }

    private fun findMaxPressure(
        current: String,
        toOpen: Set<String>,
        timeLeft: Int,
        pressure: Int = 0,
        allValves: Map<String, Valve>
    ): Int {
        return if (timeLeft <= 1) {
            pressure
        } else if (toOpen.isEmpty()) {
            pressure
        } else {
            val cur = allValves[current]!!
            toOpen.maxOfOrNull {
                val dist = cur.valves[it]!!
                val newTimeLeft = timeLeft - dist - 1
                if (newTimeLeft > 0) {
                    findMaxPressure(
                        it,
                        toOpen.minus(it),
                        newTimeLeft,
                        pressure + newTimeLeft * allValves[it]!!.flowRate,
                        allValves
                    )
                } else {
                    pressure
                }
            } ?: pressure
        }
    }


    data class Valve(val name: String, val flowRate: Int, val valves: Map<String, Int>) {

        companion object {
            private val regex = Regex("Valve ([A-Z]{2}) has flow rate=(\\d*); tunnels? leads? to valves? (.*)")
            fun of(input: String): Valve {
                val (name, flowRate, valves) = regex.matchEntire(input)!!.destructured
                return Valve(name, flowRate.toInt(), valves.split(", ").associateWith { 1 })
            }
        }
    }
    private fun Map<String,Valve>.getAllNonZeroFlowRateValves(): List<String> {
        return values.filter { it.flowRate > 0 }.map { it.name }
    }

}