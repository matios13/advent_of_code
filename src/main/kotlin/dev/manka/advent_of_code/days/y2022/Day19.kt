package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day


class Day19 : Day(19, "Not Enough Minerals", 2022) {
    override fun partOne(): String {
        return inputList.map { Blueprint.parse(it) }
            .sumOf { println(it.id.toString()+ "/ 30"); calculate(it, State(24), MutableList(24) { 0 }, MutableList(24) { 0 }) * it.id }
            .toString()
    }

    override fun partTwo(): String {
        val blueprints = inputList.map { Blueprint.parse(it) }
        return blueprints.take(3).map { println("${it.id}/3");calculate(it, State(32), MutableList(32) { 0 }, MutableList(32) { 0 }) }
            .fold(1) { acc, i -> acc * i }.toString()
    }

    data class Blueprint(
        val id: Int,
        val oreRobotCost: Int,
        val clayRobotCost: Int,
        val obsidianRobotCostOre: Int, val obsidianRobotCostClay: Int,
        val geodeRobotCostOre: Int, val geodeRobotCostObsidian: Int
    ) {
        companion object {
            private val regex =
                Regex("Blueprint (\\d+): Each ore robot costs (\\d) ore. Each clay robot costs (\\d) ore. Each obsidian robot costs (\\d) ore and (\\d+) clay. Each geode robot costs (\\d) ore and (\\d+) obsidian.")

            fun parse(line: String): Blueprint {
                val match = regex.matchEntire(line)!!.destructured.toList()
                return Blueprint(
                    match[0].toInt(),
                    match[1].toInt(),
                    match[2].toInt(),
                    match[3].toInt(),
                    match[4].toInt(),
                    match[5].toInt(),
                    match[6].toInt()
                )
            }
        }
    }

    enum class RobotType {
        ORE, CLAY, OBSIDIAN, GEODE, NONE
    }

    data class State(
        val minutes: Int,
        val oreRobots: Int = 1,
        val clayRobots: Int = 0,
        val obsidianRobots: Int = 0,
        val geodeRobots: Int = 0,
        val ore: Int = 0,
        val clay: Int = 0,
        val obsidian: Int = 0,
        val geodes: Int = 0
    ) {
        fun maxPossibleGeodes(): Int {
            return minutes * (minutes + geodeRobots) + geodes
        }

        fun stateForNextMinuteAndBuildIfPossible(robotType: RobotType, blueprint: Blueprint): State? {
            val nextMinute = copy(
                minutes = minutes - 1,
                ore = ore + oreRobots,
                clay = clay + clayRobots,
                obsidian = obsidian + obsidianRobots,
                geodes = geodes + geodeRobots
            )
            return when (robotType) {
                RobotType.ORE -> {
                    if (ore >= blueprint.oreRobotCost) {
                        nextMinute.copy(oreRobots = oreRobots + 1, ore = nextMinute.ore - blueprint.oreRobotCost)
                    } else {
                        null
                    }
                }

                RobotType.CLAY -> {
                    if (ore >= blueprint.clayRobotCost) {
                        nextMinute.copy(
                            clayRobots = clayRobots + 1,
                            ore = nextMinute.ore - blueprint.clayRobotCost
                        )
                    } else {
                        null
                    }
                }

                RobotType.OBSIDIAN -> {
                    if (ore >= blueprint.obsidianRobotCostOre && clay >= blueprint.obsidianRobotCostClay) {
                        nextMinute.copy(
                            obsidianRobots = obsidianRobots + 1,
                            ore = nextMinute.ore - blueprint.obsidianRobotCostOre,
                            clay = nextMinute.clay - blueprint.obsidianRobotCostClay
                        )
                    } else {
                        null
                    }
                }

                RobotType.GEODE -> {
                    if (ore >= blueprint.geodeRobotCostOre && obsidian >= blueprint.geodeRobotCostObsidian) {
                        nextMinute.copy(
                            geodeRobots = geodeRobots + 1,
                            ore = nextMinute.ore - blueprint.geodeRobotCostOre,
                            obsidian = nextMinute.obsidian - blueprint.geodeRobotCostObsidian
                        )
                    } else {
                        null
                    }
                }

                RobotType.NONE -> {
                    return nextMinute
                }
            }
        }
    }

    private fun calculate(
        blueprint: Blueprint,
        state: State,
        maxGeodes: MutableList<Int>,
        maxGeodeRobots: MutableList<Int>,
        seen: MutableMap<State, Int> = mutableMapOf(),
        maxSeen: MutableList<Int> = mutableListOf(0)
    ): Int {
        if (state.minutes == 0) {
            return state.geodes
        }

        if (state in seen) {
            return seen[state]!!
        }

        if (state.maxPossibleGeodes() < maxSeen[0] || shouldFinish(maxGeodeRobots, state, maxGeodes)) {
            return 0
        }

        if (state.geodeRobots > maxGeodeRobots[state.minutes - 1]) {
            maxGeodeRobots[state.minutes - 1] = state.geodeRobots
        }
        if (state.geodes > maxGeodes[state.minutes - 1]) {
            maxGeodes[state.minutes - 1] = state.geodes
        }


        val max = RobotType.values().mapNotNull { state.stateForNextMinuteAndBuildIfPossible(it, blueprint) }
            .map { calculate(blueprint, it, maxGeodes, maxGeodeRobots, seen, maxSeen) }.maxOf { it }
        seen[state] = max

        if (max > maxSeen[0]) {
            maxSeen[0] = max
        }

        return max
    }

    private fun shouldFinish(
        maxGeodeRobots: MutableList<Int>,
        state: State,
        maxGeodes: MutableList<Int>
    ) = maxGeodeRobots[state.minutes - 1] - state.geodeRobots > 1 || maxGeodes[state.minutes - 1] - state.geodes > 1

}



