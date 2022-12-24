package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import java.awt.Point
import dev.manka.advent_of_code.days.commons.*

class Day23 : Day(23, "Unstable Diffusion", 2022) {

    override fun partOne(): String {
        val elves = Elves.fromInput(inputList)
        repeat(10) {
            elves.moveOneRoundAndCheckIfNotEmpty()
        }
        val xLength = elves.positions.maxOf { it.x } - elves.positions.minOf { it.x } + 1
        val yLength = elves.positions.maxOf { it.y } - elves.positions.minOf { it.y } + 1
        return (xLength * yLength - elves.positions.size).toString()
    }

    override fun partTwo(): String {
        val elves = Elves.fromInput(inputList)
        return (generateSequence { elves.moveOneRoundAndCheckIfNotEmpty() }.takeWhile { it }.count() + 1).toString()
    }


    class Elves(val positions: MutableSet<Point>) {
        private val directions = ArrayDeque(Move.values().toList())
        fun moveOneRoundAndCheckIfNotEmpty(): Boolean {
            val moves = positions.asSequence()
                .filter { elf -> elf.arround().any { positions.contains(it) } }
                .map { elf ->
                    elf to directions.firstOrNull { direction ->
                        direction.getRestrictedPositionsFor(elf).none { positions.contains(it) }
                    }?.getPointToMoveTo(elf)
                }.filter { it.second != null }
                .groupBy { it.second }
                .filter { it.value.size == 1 }.map { it.value.first() }

            moves.forEach { (oldPos, newPos) ->
                positions.remove(oldPos)
                positions.add(newPos!!)
            }

            directions.rotate()
            return moves.isNotEmpty()
        }

        companion object {
            fun fromInput(input: List<String>): Elves {
                return input.flatMapIndexed { y, line ->
                    line.mapIndexedNotNull { x, c -> if (c == '#') Point(x, y) else null }
                }.toMutableSet().let { Elves(it) }
            }
        }
    }


    enum class Move {
        NORTH, SOUTH, WEST, EAST;

        fun getRestrictedPositionsFor(point: Point): List<Point> {
            return when (this) {
                NORTH -> listOf(
                    Point(point.x, point.y - 1),
                    Point(point.x - 1, point.y - 1),
                    Point(point.x + 1, point.y - 1)
                )

                SOUTH -> listOf(
                    Point(point.x, point.y + 1),
                    Point(point.x - 1, point.y + 1),
                    Point(point.x + 1, point.y + 1)
                )

                WEST -> listOf(
                    Point(point.x - 1, point.y),
                    Point(point.x - 1, point.y - 1),
                    Point(point.x - 1, point.y + 1)
                )

                EAST -> listOf(
                    Point(point.x + 1, point.y),
                    Point(point.x + 1, point.y - 1),
                    Point(point.x + 1, point.y + 1)
                )
            }
        }

        fun getPointToMoveTo(point: Point): Point {
            return when (this) {
                NORTH -> point.copy().moveY(-1)
                SOUTH -> point.copy().moveY(1)
                WEST -> point.copy().moveX(-1)
                EAST -> point.copy().moveX(1)
            }
        }
    }
}