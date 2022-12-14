package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.below
import dev.manka.advent_of_code.days.commons.bottomLeft
import dev.manka.advent_of_code.days.commons.bottomRight
import dev.manka.advent_of_code.days.commons.moveY
import java.awt.Point
import kotlin.math.max
import kotlin.math.min

class Day14 : Day(14, "Regolith Reservoir", 2022) {
    override fun partOne(): String {
        val rocks: MutableSet<Point> = parseRockFormations()
        val maxY = rocks.maxOf { it.y }
        return calculateNrOfSands(maxY, rocks).toString()
    }


    override fun partTwo(): String {
        val rocks = parseRockFormations()
        val maxY = rocks.maxOf { it.y } + 2
        (-500..1000).forEach { x -> rocks.add(Point(x, maxY)) }
        return calculateNrOfSands(maxY, rocks).toString()
    }

    private fun calculateNrOfSands(maxY: Int, rocks: MutableSet<Point>): Int {
        val previousSize = rocks.size
        var dropedFromLedge = false
        while (!dropedFromLedge && !rocks.contains(Point(500, 0))) {
            val sand = Point(500, 0)
            while (sand.y <= maxY && rocks.canMove(sand)) {
                moveSand(rocks, sand)
            }
            if (sand.y <= maxY) {
                rocks.add(sand)
            } else {
                dropedFromLedge = true
            }

        }
        return rocks.size - previousSize
    }

    private fun moveSand(rocks: MutableSet<Point>, sand: Point) {
        if (!rocks.contains(sand.below())) {
            sand.moveY(1)
        } else if (!rocks.contains(sand.bottomLeft())) {
            sand.translate(-1, 1)
        } else if (!rocks.contains(sand.bottomRight())) {
            sand.translate(1, 1)
        }
    }


    private fun parseRockFormations(): MutableSet<Point> {
        return inputList.asSequence()
            .flatMap { parseLine(it) }
            .toMutableSet()
    }

    private fun parseLine(line: String): Sequence<Point> {
        return line.split(" -> ")
            .asSequence()
            .map { it.split(",") }
            .map {
                Point(Integer.parseInt(it[0]), Integer.parseInt(it[1]))

            }.windowed(2)
            .flatMap {
                if (it[0].x == it[1].x) {
                    allYBetweenTwoPoints(it).map { i -> Point(it[0].x, i) }
                } else {
                    allXBetweenTwoPoints(it).map { i -> Point(i, it[0].y) }
                }
            }
    }

    private fun allXBetweenTwoPoints(it: List<Point>) = (min(it[0].x, it[1].x)..max(it[0].x, it[1].x))

    private fun allYBetweenTwoPoints(it: List<Point>) = (min(it[0].y, it[1].y)..max(it[0].y, it[1].y))

    private fun Set<Point>.canMove(point: Point) =
        !this.contains(point.below()) || !this.contains(point.bottomLeft()) || !this.contains(point.bottomRight())
}