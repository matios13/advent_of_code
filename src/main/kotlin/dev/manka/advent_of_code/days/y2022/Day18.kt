package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.Point3D
import kotlin.math.abs


class Day18: Day(18, "Boiling Boulders", 2022) {
    override fun partOne(): String {
        val inputPoints = parseInput()
        return inputPoints.sumOf { point -> getSumOfAllUniqueFaces(point,inputPoints) }.toString()
    }

    override fun partTwo(): String {
        val inputPoints = parseInput().toSet()
        var total = 0
        inputPoints.forEachIndexed{ index, point ->
            println("Checking $point total: $total index: $index/${inputPoints.size}")
            for (face in faces) {
                val sum = point + face

                if (sum !in inputPoints && canEscape(inputPoints, sum)) {
                    total += 1
                }
            }
        }


        return total.toString()
    }

    private fun parseInput(): List<Point3D> {
        return inputList
            .map { it.split(",").map { i -> i.toInt() } }
            .map { Point3D(it[0], it[1], it[2]) }
    }

    private val faces = setOf(
        Point3D(1, 0, 0),
        Point3D(-1, 0, 0),
        Point3D(0, 1, 0),
        Point3D(0, -1, 0),
        Point3D(0, 0, 1),
        Point3D(0, 0, -1),
    )

    private fun getSumOfAllUniqueFaces(point: Point3D, inputPoints: List<Point3D>): Int = faces.count { face -> !inputPoints.contains(point + face) }

    private fun canEscape(input: Set<Point3D>, pos: Point3D, visited: MutableSet<Point3D> = mutableSetOf()): Boolean {
        visited.add(pos)
        return Point3D.Coordinate.values().any { coordinate ->
            Point3D.Direction.values().any { direction ->
                val collision = input
                    .filter { pos.collides(it, coordinate, direction) }
                    .minByOrNull { abs(pos.coordinate(coordinate) - it.coordinate(coordinate)) } ?: return true

                val start = collision.movedTo(coordinate, collision.coordinate(coordinate) - direction.value)
                start !in visited && canEscape(input, start, visited)
            }
        }
    }
}



