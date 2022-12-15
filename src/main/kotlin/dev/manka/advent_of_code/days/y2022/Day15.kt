package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import kotlin.math.absoluteValue

private const val  MAX_COORDINATE= 4_000_000L
class Day15 : Day(15,"" ,2022) {
    override fun partOne(): String {
        val area = inputList.map { Sensor.of(it) }
        return area.countNonDistressSignalInRow(2_000_000).toString()
    }
    override fun partTwo(): String {
        val safeRangesPerLine = (0L..MAX_COORDINATE)
            .map { row ->
                inputList.map { Sensor.of(it) }
                    .mapNotNull{it.detectableRangeOrNull(row)
                    }
            }
        return calculateTuningFrequency(safeRangesPerLine).toString()
    }

    private fun List<Sensor>.countNonDistressSignalInRow(row: Long): Int {
        val maxXOfSet = this.maxOf { this.maxOf { s -> (s.x - s.beaconX).absoluteValue } }
        val minX = this.minOf { listOf(it.x - maxXOfSet, it.beaconX, 0).min() }
        val maxX = this.maxOf { listOf(it.x + maxXOfSet, it.beaconX, 0).max() }
        return (minX..maxX).count { x ->
            this.filterNot { it.beaconX == x && it.beaconY == row }
                .any { it.manhattanDistance >= manhattanDistance(x, row, it.x, it.y) }
        }
    }

    private fun calculateTuningFrequency(detectableRangesPerLine: List<List<LongRange>>) : Long{
        detectableRangesPerLine.forEachIndexed { y, ranges ->
            var currentTop = ranges.minBy { it.first }.last
            ranges.sortedBy { it.first }.drop(1).forEach {
                if (it.first > currentTop) {
                    return ((it.first - 1) * MAX_COORDINATE + y)
                }
                if (it.last > currentTop) {
                    currentTop = it.last
                }
            }
        }
        return -1
    }

    class Sensor(val x: Long, val y: Long, val beaconX: Long, val beaconY: Long) {
        val manhattanDistance = manhattanDistance(x, y, beaconX, beaconY)
        fun detectableRangeOrNull(row: Long): LongRange? {
            val width = manhattanDistance - (y - row).absoluteValue
            return if (width > 0) {
                return (x - width..x + width)
            } else null
        }

        companion object {
            private val regex = Regex("\\D+(\\d+)\\D+(\\d+)\\D+(\\d+)\\D+(\\d+)")
            fun of(input: String): Sensor {
                val (x, y, foundX, foundY) = regex.matchEntire(input)!!.destructured
                return Sensor(x.toLong(), y.toLong(), foundX.toLong(), foundY.toLong())
            }
        }
    }
}

private fun manhattanDistance(x: Long, y: Long, beaconX: Long, beaconY: Long) =
    (x - beaconX).absoluteValue + (y - beaconY).absoluteValue
