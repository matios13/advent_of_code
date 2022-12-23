package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.copy
import java.awt.Point

class Day22 : Day(22, "Monkey Map", 2022) {
    private val board = inputList.take(inputList.size - 2)
    private val listOfMoves = parseMoves(inputList.last())

    override fun partOne(): String {
        val initialPosition = Point(board[0].indexOf('.'), 0)
        val wrappedPoints = buildWrappedPointsMap(initialPosition)
        return move(wrappedPoints, initialPosition).toString()
    }

    override fun partTwo(): String {
        val initialPosition = Point(board[0].indexOf('.'), 0)
        val borders = buildBorders(initialPosition)
        val map = buildMap {
            val sideLength = calculateSideLength(borders)
            var unpairedEdges: List<Pair<Direction, List<Pair<Direction, Point>>>> =
                MutableList(borders.size / sideLength) { i ->
                    borders[i * sideLength].first to borders.subList(i * sideLength, (i + 1) * sideLength)
                }
            while (unpairedEdges.isNotEmpty()) {
                unpairedEdges.windowed(2).forEachIndexed { index, window ->
                    val (edgeA, edgeB) = window
                    if (unpairedEdges.contains(edgeA) && edgeA.first.isRightOf(edgeB.first)) {
                        (0 until sideLength).forEach {
                            val (direction1, position1) = edgeA.second[it]
                            val (direction2, position2) = edgeB.second[sideLength - it - 1]
                            put(direction1.turnLeft() to position1, direction2.turnRight() to position2)
                            put(direction2.turnLeft() to position2, direction1.turnRight() to position1)
                        }
                        unpairedEdges = removeTwoAndRotate(unpairedEdges, index)
                    }
                }
            }
        }
        return move(map, initialPosition).toString()
    }

    private fun removeTwoAndRotate(
        unpairedEdges: List<Pair<Direction, List<Pair<Direction, Point>>>>, i: Int
    ): List<Pair<Direction, List<Pair<Direction, Point>>>> =
        unpairedEdges.subList(0, i) + unpairedEdges.subList(i + 2, unpairedEdges.size)
            .map { it.first.turnLeft() to it.second }


    private fun calculateSideLength(borders: List<Pair<Direction, Point>>): Int {
        var sideLength = Int.MAX_VALUE
        var previousDirection = Direction.EAST
        var directionCount = 0
        for ((direction, _) in borders) {
            if (direction == previousDirection) {
                directionCount++
            } else {
                sideLength = minOf(sideLength, directionCount)
                previousDirection = direction
                directionCount = 1
            }
        }
        return sideLength
    }

    private fun buildWrappedPointsMap(initialPosition: Point): Map<Pair<Direction, Point>, Pair<Direction, Point>> {
        return buildBorders(initialPosition).associate { (dir, po) ->
            val wrappedDirection = dir.turnLeft()
            val wrappedPoint = po.copy()
            when (wrappedDirection) {
                Direction.EAST -> wrappedPoint.x = board[wrappedPoint.y].indexOfFirst { it != ' ' }
                Direction.NORTH -> wrappedPoint.y =
                    board.indexOfFirst { wrappedPoint.x in it.indices && it[wrappedPoint.x] != ' ' }

                Direction.WEST -> wrappedPoint.x = board[wrappedPoint.y].indexOfLast { it != ' ' }
                Direction.SOUTH -> wrappedPoint.y =
                    board.indexOfLast { wrappedPoint.x in it.indices && it[wrappedPoint.x] != ' ' }
            }
            Pair(wrappedDirection, po) to Pair(wrappedDirection, wrappedPoint)
        }
    }

    private fun move(wrappedPositions: Map<Pair<Direction, Point>, Pair<Direction, Point>>, point: Point): Int {
        val (direction, position) = listOfMoves.fold(Direction.EAST to point) { state, move ->
            when (move) {
                is Move.Forward -> {
                    moveForward(move, state, wrappedPositions)
                }

                Move.Left -> state.copy(first = state.first.turnLeft())
                Move.Right -> state.copy(first = state.first.turnRight())
            }
        }
        return 1000 * (position.y + 1) + 4 * (position.x + 1) + direction.ordinal
    }

    private fun moveForward(
        move: Move.Forward,
        state: Pair<Direction, Point>,
        wrappedPositions: Map<Pair<Direction, Point>, Pair<Direction, Point>>
    ): Pair<Direction, Point> {
        var nextState = state.copy()
        repeat(move.count) {
            val tempState =
                wrappedPositions.getOrElse(nextState) { nextState.copy(second = nextState.second.moveOnce(nextState.first)) }
            if (board[tempState.second] != '.') return nextState
            nextState = tempState
        }
        return nextState
    }

    private sealed class Move {
        data class Forward(val count: Int) : Move()
        object Left : Move()
        object Right : Move()
    }

    private fun parseMoves(line: String) = """(\d+)|(L)|(R)""".toRegex().findAll(line).mapNotNull {
            it.groups[1]?.value?.toIntOrNull()?.let { i -> Move.Forward(i) } ?: it.groups[2]?.let { Move.Left }
            ?: it.groups[3]?.let { Move.Right }
        }.toList()


    private operator fun List<String>.get(position: Point): Char = getOrNull(position.y)?.getOrNull(position.x) ?: ' '

    private fun buildBorders(initialPosition: Point): List<Pair<Direction, Point>> {
        return buildList {
            var position = initialPosition
            var direction = Direction.EAST
            do {
                add(direction to position)
                val forward = position.moveOnce(direction)
                if (board[forward] == ' ') {
                    direction = (direction.turnRight())
                } else {
                    val left = forward.moveOnce(direction.turnLeft())
                    if (board[left] == ' ') {
                        position = forward
                    } else {
                        position = left
                        direction = (direction.turnLeft())
                    }
                }
            } while (position != initialPosition || direction != Direction.EAST)
        }
    }

    private enum class Direction {
        EAST, NORTH, WEST, SOUTH;

        fun turnLeft() = values()[(ordinal + 3) % 4]
        fun turnRight() = values()[(ordinal + 1) % 4]
        fun isRightOf(other: Direction) = (ordinal - other.ordinal).mod(4) == 1
    }

    private fun Point.moveOnce(direction: Direction): Point = when (direction) {
        Direction.EAST -> Point(x + 1, y)
        Direction.NORTH -> Point(x, y + 1)
        Direction.WEST -> Point(x - 1, y)
        Direction.SOUTH -> Point(x, y - 1)
    }
}