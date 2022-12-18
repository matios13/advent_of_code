package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import java.awt.Point
import java.util.*

class Day17 : Day(17, "Pyroclastic Flow", 2022) {
    private var gasDirections = inputToDeque()
    override fun partOne(): String {
        val chamber: MutableList<MutableList<Boolean>> = mutableListOf(emptyLine(7))
        (0 until 2022)
            .map { Rock.values()[it % Rock.values().size] }
            .forEach {
                dropRock(chamber, it)
            }
        return (chamber.highestPoint()).toString()
    }

    override fun partTwo() : String{
        return ""
    }


    private fun dropRock(
        chamber: MutableList<MutableList<Boolean>>,
        it: Rock
    ) {
        val startY = chamber.highestPoint() + 3
        val positionOfTheRock = Point(2, startY)
        (0..startY + 4).forEach { i ->
            if (chamber.size <= i) chamber.add(emptyLine(7))
        }

        while (positionOfTheRock.y >= 0 && !it.willItHit(chamber, positionOfTheRock)) {
            if (gasDirections.isEmpty()) gasDirections = inputToDeque()
            moveRock(gasDirections, positionOfTheRock, it, chamber)
            positionOfTheRock.y--
        }
        positionOfTheRock.y++

        it.shape.reversed().forEach { row ->
            row.forEach { column ->
                if (column) {
                    chamber[positionOfTheRock.y][positionOfTheRock.x] = true
                }
                positionOfTheRock.x++
            }
            positionOfTheRock.x -= it.width()
            positionOfTheRock.y++
        }
    }

    private fun moveRock(
        gasDirections: Deque<Direction>,
        positionOfTheRock: Point,
        rock: Rock,
        chamber: MutableList<MutableList<Boolean>>
    ) {
        val move = gasDirections.poll()
        val oldX = positionOfTheRock.x
        if (move == Direction.LEFT && positionOfTheRock.x > 0) {
            positionOfTheRock.x--
        } else if (move == Direction.RIGHT && positionOfTheRock.x + rock.width() < 7) {
            positionOfTheRock.x++
        }
        if(rock.willItHit(chamber, positionOfTheRock)){
            positionOfTheRock.x = oldX
        }
    }

    private fun emptyLine(length: Int) = MutableList(length) { false }

    private fun MutableList<MutableList<Boolean>>.print() {
        reversed().forEach {
            println(it.joinToString("") { b -> if (b) "#" else "." })
        }
    }
    private fun MutableList<MutableList<Boolean>>.highestPoint() =
        indexOfFirst { it.all { b -> !b } }

    private fun inputToDeque(): Deque<Direction> = inputList[0]
        .map { if (it == '<') Direction.LEFT else Direction.RIGHT }
        .toCollection(LinkedList())

    private enum class Direction {
        LEFT, RIGHT
    }

    private enum class Rock(val shape: List<List<Boolean>>) {
        FLAT(listOf(listOf(true, true, true, true))),
        CROSS(listOf(listOf(false, true, false), listOf(true, true, true), listOf(false, true, false))),
        REVERSE_L(listOf(listOf(false, false, true), listOf(false, false, true), listOf(true, true, true))),
        VERTICAL(listOf(listOf(true), listOf(true), listOf(true), listOf(true))),
        SQUARE(listOf(listOf(true, true), listOf(true, true)));

        fun willItHit(chamber: List<List<Boolean>>, startPoint: Point): Boolean {
            val currentPoint = Point(startPoint)
            shape.reversed().forEach { row ->
                row.forEach { column ->
                    if (column) {
                        if (currentPoint.y<chamber.size && chamber[currentPoint.y][currentPoint.x]) {
                            return true
                        }
                    }
                    currentPoint.x++
                }
                currentPoint.x -= width()
                currentPoint.y++
            }
            return false
        }

        fun width() = shape[0].size
    }
}