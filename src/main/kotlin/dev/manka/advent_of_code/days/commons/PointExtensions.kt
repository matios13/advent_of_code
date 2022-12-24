package dev.manka.advent_of_code.days.commons

import java.awt.Point

fun Point.moveX(i: Int): Point {
    x += i
    return this
}

fun Point.moveY(i: Int): Point {
    y += i
    return this
}

fun Point.moveToX(i: Int): Point {
    x = i
    return this
}
fun Point.moveXInRange(i: Int, range: Int): Point{
    x = (x+i).mod(range)
    return this
}

fun Point.moveYInRange(i: Int, range: Int): Point{
    y = (y+i).mod(range)
    return this
}

fun Point.moveToY(i: Int): Point {
    y = i
    return this
}

fun Point.below() = Point(x, y + 1)
fun Point.bottomRight() = Point(x + 1, y + 1)
fun Point.bottomLeft() = Point(x - 1, y + 1)

fun Point.nearestPoints(): List<Point> {
    return listOf(Point(x - 1, y), Point(x, y - 1), Point(x + 1, y), Point(x, y + 1)).filter { it.x >= 0 && it.y >= 0 }
}
fun Point.isInRange(maxX: Int, maxY: Int, minX: Int = 0, minY: Int = 0) = x in minX until maxX && y in minY until maxY

fun Point.nearestPointsWithin(xSize: Int, ySize: Int): List<Point> {
    return nearestPoints().filter { it.x < xSize && it.y < ySize }
}

fun Point.arround(): List<Point> {
    return listOf(
        Point(x - 1, y),
        Point(x, y - 1),
        Point(x + 1, y),
        Point(x, y + 1),
        Point(x - 1, y - 1),
        Point(x + 1, y - 1),
        Point(x - 1, y + 1),
        Point(x + 1, y + 1)
    )
}

fun Point.copy() = Point(x, y)
operator fun Point.component1(): Int = x
operator fun Point.component2(): Int = y