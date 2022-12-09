package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.moveX
import dev.manka.advent_of_code.days.commons.moveY
import java.awt.Point
import kotlin.math.absoluteValue
import kotlin.math.sign

class Day09 :Day(9, "Treetop Tree House", 2022) {
    override fun partOne()= headMoves.moveTail().distinctSize().toString()


    override fun partTwo()=
         headMoves
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .moveTail()
            .distinctSize().toString()


    private val headMoves = sequence {
        val headPosition = Point(0, 0)

        yield( headPosition)
        for (line in inputList) {
            val amount = line.drop(2).toInt()
            when (line[0]) {
                'L' -> repeat(amount) { yield(headPosition.moveX(-1)) }
                'U' -> repeat(amount) { yield(headPosition.moveY(-1))}
                'R' -> repeat(amount) { yield(headPosition.moveX(1))}
                'D' -> repeat(amount) { yield(headPosition.moveY(1))}
            }
        }
    }
}

private fun Sequence<Point>.moveTail(): Sequence<Point> = sequence {
    val tail = Point(0, 0)
    yield(tail)
    forEach {
        val deltaX = it.x - tail.x
        val deltaY = it.y - tail.y
        if (deltaX.absoluteValue > 1 || deltaY.absoluteValue > 1) {
            tail.x = if (deltaX.absoluteValue >= deltaY.absoluteValue) it.x - deltaX.sign else it.x
            tail.y = if (deltaX.absoluteValue <= deltaY.absoluteValue) it.y - deltaY.sign else it.y
            yield(Point(tail))
        }
    }
}

private fun Sequence<*>.distinctSize(): Int = toSet().size

