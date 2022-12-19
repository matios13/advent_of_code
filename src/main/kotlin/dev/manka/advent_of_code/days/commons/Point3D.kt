package dev.manka.advent_of_code.days.commons

data class Point3D (val x: Int, val y: Int, val z: Int) {
    operator fun plus(other: Point3D): Point3D {
        return Point3D(x + other.x, y + other.y, z + other.z)
    }

    fun coordinate(coordinate: Coordinate): Int {
        return when (coordinate) {
            Coordinate.X -> x
            Coordinate.Y -> y
            Coordinate.Z -> z
        }
    }

    fun movedTo(coordinate: Coordinate, value: Int): Point3D {
        return when(coordinate) {
            Coordinate.X -> Point3D(value, y, z)
            Coordinate.Y -> Point3D(x, value, z)
            Coordinate.Z -> Point3D(x, y, value)
        }
    }

    fun collides(other: Point3D, coordinate: Coordinate, direction: Direction): Boolean {
        val sameColumn = Coordinate.values().filter { it!=coordinate }.all { coordinate(it) == other.coordinate(it) }

        return sameColumn && when (direction) {
            Direction.NEGATIVE -> other.coordinate(coordinate) < coordinate(coordinate)
            Direction.POSITIVE-> other.coordinate(coordinate) > coordinate(coordinate)
        }
    }
    enum class Coordinate {
        X, Y, Z
    }
    enum class Direction(val value: Int) {
        POSITIVE(1), NEGATIVE(-1)
    }
}