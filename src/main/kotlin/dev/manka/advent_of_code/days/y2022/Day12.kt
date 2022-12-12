package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.commons.nearestPointsWithin
import java.awt.Point

class Day12 : Day(12, "", 2022) {

    override fun partOne(): String {
        val parsedInput = getFieldsWithStartPoints(inputList)
        val weights = traverseAndGetWeights(parsedInput)
        return weights[parsedInput.startPoint]!!.toString()
    }

    override fun partTwo(): String {
        val parsedInput = getFieldsWithStartPoints(inputList)
        val weights = traverseAndGetWeights(parsedInput)

        return getAllPossibleStarts(parsedInput.fields)
            .mapNotNull { weights[it] }
            .min()
            .toString()
    }

    private fun getAllPossibleStarts(field: List<List<Char>>) =
        field.flatMapIndexed { i, line ->
            line.mapIndexedNotNull { j, char ->
                if (char == 'a') Point(i, j) else null
            }
        }

    private fun getFieldsWithStartPoints(input: List<String>): FieldsWithStartPoint {
        val fields = input.map { it.asIterable().toMutableList() }

        val startPoint = fields.mapIndexed() { i, line ->
            Point(i, line.indexOf('S'))
        }.first { it.y != -1 }

        val endPoint = fields.mapIndexed() { i, line ->
            Point(i, line.indexOf('E'))
        }.first { it.y != -1 }

        fields[startPoint.x][startPoint.y] = 'a'
        fields[endPoint.x][endPoint.y] = 'b'

        return FieldsWithStartPoint(fields, startPoint, endPoint)
    }

    private fun traverseAndGetWeights(fieldsWithStartPoint: FieldsWithStartPoint): Map<Point, Int> {

        val (fields, _, endPoint) = fieldsWithStartPoint
        val weights = mutableMapOf(endPoint to 0)
        var step = 0
        do {
            val currentPoints = weights.filter { (_, weight) -> weight == step }.keys
            step++
            currentPoints.forEach { point -> addWeightToAllReachableFields(point, fields, weights, step) }
        } while (currentPoints.isNotEmpty())
        return weights
    }

    private fun addWeightToAllReachableFields(
        point: Point,
        fields: List<List<Char>>,
        weights: MutableMap<Point, Int>,
        step: Int
    ) {
        point.nearestPointsWithin(fields.size, fields.first().size).forEach {
            if (fields[point.x][point.y] - fields[it.x][it.y] <= 1 && !weights.contains(it)) {
                weights[it] = step
            }
        }
    }

    data class FieldsWithStartPoint(val fields: List<List<Char>>, val startPoint: Point, val endPoint: Point)
}