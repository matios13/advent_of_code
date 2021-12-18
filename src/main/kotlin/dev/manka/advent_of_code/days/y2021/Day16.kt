package dev.manka.advent_of_code.days.y2021

import dev.manka.advent_of_code.days.Day


class Day16 : Day(16, "Packet Decoder") {

    override fun partOne(): String {
        return packets(inputAsCharArray().iterator()).sumVersion().toString()
    }

    override fun partTwo(): String {

        return packets(inputAsCharArray().iterator()).sumValues().toString()
    }

    private data class Packet(
        val version: Int,
        val id: Int,
        val value: Long,
        val children: List<Packet> = emptyList(),
    ) {
        fun sumVersion(): Int = version + children.sumOf { it.sumVersion() }
        fun sumValues(): Long = when (id) {
            0 -> children.sumOf { it.sumValues() }
            1 -> children.fold(1) { acc, next -> acc * next.sumValues() }
            2 -> children.minOf { it.sumValues() }
            3 -> children.maxOf { it.sumValues() }
            4 -> value
            5 -> if (children.first().sumValues() > children[1].sumValues()) 1 else 0
            6 -> if (children.first().sumValues() < children[1].sumValues()) 1 else 0
            7 -> if (children.first().sumValues() == children[1].sumValues()) 1 else 0
            else -> error("Invalid Operator type")
        }
    }

    private fun inputAsCharArray() = inputList.first()
        .split("")
        .filter { it.isNotBlank() }
        .joinToString("") { it.toInt(16).toString(2).padStart(4, '0') }
        .toCharArray()


    private fun packets(input: Iterator<Char>): Packet {
        val version = input.next(3).toInt(2)
        return when (val id = input.next(3).toInt(2)) {
            4 -> {
                var wholeString = ""
                do {
                    val string = input.next(5)
                    wholeString += string.drop(1)
                } while (string.startsWith("1"))
                val value = wholeString.toLong(2)
                Packet(version, id, value)
            }
            else -> {
                operatorPacket(input, version, id)
            }
        }
    }

    private fun operatorPacket(input: Iterator<Char>, version: Int, id: Int): Packet {
        return when (input.next(1).toInt(2)) { // Length Type
            0 -> {
                val subPacketLength = input.next(15).toInt(2)
                val subPacketIterator = input.next(subPacketLength).iterator()
                val subPackets = mutableListOf<Packet>()
                while (subPacketIterator.hasNext()) {
                    subPackets.add(packets(subPacketIterator))
                }
                Packet(version, id, 0, subPackets)
            }
            1 -> {
                val numberOfPackets = input.next(11).toInt(2)
                val subPackets = (1..numberOfPackets).map { packets(input) }
                Packet(version, id, 0, subPackets)
            }
            else -> error("Invalid Operator length type")
        }

    }

    fun Iterator<Char>.next(size: Int): String = (1..size).map { next() }.joinToString("")

}




