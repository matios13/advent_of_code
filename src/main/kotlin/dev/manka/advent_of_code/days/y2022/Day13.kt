package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day13 : Day(13, "Distress Signal", 2022) {

    override fun partOne(): String {
        val pairs = inputList.windowed(2, 3).map { parsePackets(it[0]).first to parsePackets(it[1]).first }
        return pairs.mapIndexed { index, pair -> if (pair.first < pair.second) index + 1 else 0 }.sum().toString()
    }

    override fun partTwo(): String {
        val packet2 = parsePackets("[[2]]").first
        val packet6 = parsePackets("[[6]]").first
        val packets = inputList
            .windowed(2, 3)
            .flatMap { listOf(parsePackets(it[0]).first, parsePackets(it[1]).first) }
            .plus(packet2)
            .plus(packet6)
            .sorted()

        return ((packets.indexOf(packet2) + 1) * (packets.indexOf(packet6) + 1)).toString()
    }

    private fun parsePackets(line: String): Pair<Packet, String> {
        if (line[0] != '[') {
            val num = line.takeWhile { it.isDigit() }
            return Packet.IntPacket(num.toInt()) to line.drop(num.length)
        }
        val packets = mutableListOf<Packet>()
        var remainingLine = line.drop(1)
        while (remainingLine.isNotEmpty()) {
            if (remainingLine[0] == ']') {
                return Packet.ListPacket(packets) to remainingLine.drop(1)
            } else if (remainingLine[0] == ',') {
                remainingLine = remainingLine.drop(1)
            }
            val (packet, rest) = parsePackets(remainingLine)
            packets += packet
            remainingLine = rest
        }
        return Packet.ListPacket(packets) to ""
    }
}

sealed interface Packet : Comparable<Packet> {
    data class IntPacket(val value: Int) : Packet {
        override fun toListPacket(): ListPacket = ListPacket(listOf(this))
    }

    data class ListPacket(val value: List<Packet>) : Packet {
        override fun toListPacket(): ListPacket = this
    }

    fun toListPacket(): ListPacket

    override fun compareTo(other: Packet): Int {
        return when (this) {
            is IntPacket -> when (other) {
                is IntPacket -> this.value.compareTo(other.value)
                is ListPacket -> {
                    this.toListPacket().compareTo(other)
                }
            }

            is ListPacket -> when (other) {
                is IntPacket -> {
                    this.compareTo(other.toListPacket())
                }

                is ListPacket -> {
                    val comparedLists = this.value.zip(other.value)
                        .map { zipped -> zipped.first.compareTo(zipped.second) }
                        .firstOrNull { it != 0 } ?: 0

                    if (comparedLists == 0) {
                        this.value.size.compareTo(other.value.size)
                    } else {
                        comparedLists
                    }
                }
            }
        }
    }

}



