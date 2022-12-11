package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day11 : Day(11, "Monkey in the Middle", 2022) {

    override fun partOne(): String {
        return throwItemsInRounds(getMonkeys(), 20, 3).toString()
    }

    override fun partTwo(): String {
        return throwItemsInRounds(getMonkeys(), 10000).toString()
    }

    private fun getMonkeys(): List<Monkey> {
        return inputList.chunked(7).map { Monkey(it) }
    }

    private fun throwItemsInRounds(monkeys: List<Monkey>, rounds: Int, divideBy: Int? = null): Long {
        val mod = monkeys.map { it.divideBy }.reduce(Int::times)
        repeat(rounds) {
            throwItems(monkeys, divideBy, mod)
        }
        return monkeys.map { it.inspectionCount }.sortedDescending().let { it[0] * it[1] }
    }

    private fun throwItems(monkeys: List<Monkey>, divideBy: Int?, mod: Int) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { old ->
                monkey.inspectionCount++
                val worry = if (divideBy == null) monkey.calculate(old) % mod else monkey.calculate(old) / divideBy
                monkeys[monkey.getMonkeyToThrowTo(worry)].items += worry
            }
            monkey.items.clear()
        }
    }
}

class Monkey(input: List<String>) {

    var inspectionCount = 0L
    val items = input[1].substringAfter(": ").split(", ").map { it.toLong() }.toMutableList()
    private val shouldAdd = "+" in input[2]
    val x = input[2].substringAfterLast(" ").toLongOrNull()
    val divideBy = input[3].substringAfterLast(" ").toInt()
    private val trueMonkey = input[4].substringAfterLast(" ").toInt()
    private val falseMonkey = input[5].substringAfterLast(" ").toInt()
    fun getMonkeyToThrowTo(worry: Long): Int {
        return if (worry % divideBy == 0L) trueMonkey else falseMonkey
    }

    fun calculate(old: Long): Long {
        val y = x ?: old
        return if (shouldAdd) old + y else old * y
    }
}
