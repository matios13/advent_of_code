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