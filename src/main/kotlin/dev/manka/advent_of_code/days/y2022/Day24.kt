package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.*
import java.awt.Point
import java.util.*
import kotlin.math.absoluteValue

class Day24 : Day(24, "Blizzard Basin", 2022) {

    override fun partOne(): String {
        return Basin.fromInput(inputList).moveThroughBasin().toString()
    }

    override fun partTwo(): String {
        return Basin.fromInput(inputList).let { basin ->
            val firstGo = basin.moveThroughBasin()
            val backForSnacks = basin.moveThroughBasin(basin.end, basin.start, firstGo)
            basin.moveThroughBasin(startTime = backForSnacks).toString()
        }
    }
    class Blizzard(private val startPos: Point, private val direction: Point) {
        fun positionAtTime(time: Int, xSize: Int, ySize: Int) =
            startPos.copy().moveXInRange(direction.x * time, xSize).moveYInRange(direction.y * time, ySize)

    }

    class Basin(private val blizzards: List<Blizzard>, private val xSize: Int, private val ySize: Int) {
        val start = Point(0, -1)
        val end = Point(xSize - 1, ySize)

        private fun isPositionSafe(position: Point, time: Int) =
            position == start || position == end || position.isInRange(xSize, ySize)
                    && blizzards.none { it.positionAtTime(time, xSize, ySize) == position }

        fun moveThroughBasin(start: Point = this.start, end: Point = this.end, startTime: Int = 0): Int {
            val startState = State(start, startTime)
            val toExplore = PriorityQueue<State> { a, b -> a.minimumTime(end) - b.minimumTime(end) }
                .apply { offer(startState) }
            val visited = mutableSetOf(startState)
            var endTime: Int? = null
            while (endTime == null) {
                val newVisitedPositions = getAllPossibleStates(toExplore.poll(), visited)
                newVisitedPositions.firstOrNull { it.position == end }.let { endTime = it?.time }
                newVisitedPositions.filter { isPositionSafe(it.position, it.time) }
                    .forEach { toExplore.offer(it) }
                visited.addAll(newVisitedPositions)
            }
            return endTime!!
        }

        private fun getAllPossibleStates(state: State, visited: MutableSet<State>): List<State> {
            val newTime = state.time + 1
            return Move.values().map { it.move(state.position) }.mapNotNull { newPos ->
                val newState = State(newPos, newTime)
                if (!visited.contains(newState)) {
                    newState
                } else {
                    null
                }
            }
        }

        companion object {
            fun fromInput(input: List<String>): Basin {
                val blizzards = input.mapIndexed() { row, line ->
                    line.mapIndexedNotNull() { col, c ->
                        when (c) {
                            '>' -> Blizzard(Point(col - 1, row - 1), Point(1, 0))
                            'v' -> Blizzard(Point(col - 1, row - 1), Point(0, 1))
                            '<' -> Blizzard(Point(col - 1, row - 1), Point(-1, 0))
                            '^' -> Blizzard(Point(col - 1, row - 1), Point(0, -1))
                            else -> null
                        }
                    }
                }.flatten()
                return Basin(blizzards, input.first().length - 2, input.size - 2)
            }
        }
    }

    enum class Move(val x: Int, val y: Int) {
        UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), REST(0, 0);

        fun move(point: Point) = point.copy().moveX(x).moveY(y)
    }

    data class State(val position: Point, val time: Int) {
        fun minimumTime(endPosition: Point) =
            time + (position.x - endPosition.x).absoluteValue + (position.y - endPosition.y).absoluteValue
    }
}
