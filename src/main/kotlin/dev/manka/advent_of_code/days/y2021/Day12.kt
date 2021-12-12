package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day

class Day12 : Day(12, "Passage Pathing") {
    override fun partOne(): String {
        val routes = getRoutesFromInput()

        return findPaths(listOf("start"), routes,true).count().toString()
    }

    override fun partTwo(): String {
        val routes = getRoutesFromInput()

        return findPaths(listOf("start"), routes, false).count().toString()
    }

    private fun getRoutesFromInput(): MutableMap<String, MutableList<String>> {
        val routes = inputList.map { it.split("-") }.map { it[0] to it[1] }
            .groupByTo(mutableMapOf(), { it.first }, { it.second })

        routes.map { it.value.mapNotNull { s -> if (s != "end") s to it.key else null } }
            .flatten()
            .forEach { routes.addRoute(it.first, it.second) }
        return routes
    }

    private fun findPaths(
        path: List<String>,
        routes: MutableMap<String, MutableList<String>>,
        containsTwiceSame: Boolean
    ): List<List<String>> {
        val lastPosition = path.last()
        if (lastPosition == "end") return mutableListOf(path)
        return routes[lastPosition]!!.filterNot { it == "start" }
            .filterNot { it.first().isLowerCase() && containsTwiceSame && path.contains(it) }
            .map {
                findPaths(
                    path + it,
                    routes,
                    containsTwiceSame || it.first().isLowerCase() && path.contains(it)
                )
            }.flatten()
    }

    private fun MutableMap<String, MutableList<String>>.addRoute(key: String, s: String) {
        this.putIfAbsent(key, mutableListOf(s))
        if (s !in this[key]!!) this[key]?.add(s)
    }
}
