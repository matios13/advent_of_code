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
fun Point.moveToY(i: Int): Point {
    y = i
    return this
}
fun Point.nearestPoints(): List<Point> {
    return listOf(Point(x - 1, y), Point(x, y - 1), Point(x + 1, y), Point(x, y + 1)).filter { it.x >= 0 && it.y >= 0 }
}
fun Point.nearestPointsWithin(xSize: Int, ySize: Int): List<Point> {
    return nearestPoints().filter { it.x < xSize && it.y < ySize }
}