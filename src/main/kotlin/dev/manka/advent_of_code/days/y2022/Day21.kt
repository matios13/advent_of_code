package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day21 : Day(21, "", 2022) {
    private val regex = Regex("(\\w+): ((\\d+)|(\\w+) (\\+|-|\\*|\\/) (\\w+))")
    override fun partOne(): String {
        val monkeys = inputList.map { parseInput(it) }.associateBy { it.name }
        return monkeys["root"]!!.calculateValue(monkeys).toString()
    }

    override fun partTwo(): String {
        val monkeys = inputList.map { parseInput(it) }.associateBy { it.name }
        val root = monkeys["root"]!!
        val trailToHuman = getHumanTrail(root, monkeys)!!.reversed().drop(1)
        val humanNumber = trailToHuman.windowed(2)
            .fold(initial = getOtherMonkeysNumber(monkeys, root, trailToHuman)) { acc, (cur, next) ->
                val currentMonkey = monkeys[cur]!!
                val other =
                    getOthersMonkeyValue(currentMonkey, next, monkeys)
                val isLeft = monkeys[cur]?.monkey1Name == next
                when (monkeys[cur]!!.operator!!) {
                    Operator.ADD -> acc - other
                    Operator.SUBTRACT -> if (isLeft) acc + other else other - acc
                    Operator.MULTIPLY -> acc / other
                    Operator.DIVIDE -> if (isLeft) acc * other else other / acc
                }
            }
        return humanNumber.toString()

    }

    private fun getOthersMonkeyValue(
        currentMonkey: Monkey,
        next: String,
        monkeys: Map<String, Monkey>
    ) = if (currentMonkey.monkey1Name == next)
        monkeys[currentMonkey.monkey2Name]!!.calculateValue(monkeys)
    else monkeys[currentMonkey.monkey1Name]!!.calculateValue(monkeys)

    private fun getOtherMonkeysNumber(
        monkeys: Map<String, Monkey>,
        root: Monkey,
        trailToHuman: List<String>
    ): Long {
        val monkey1 = monkeys[root.monkey1Name]!!
        val monkey2 = monkeys[root.monkey2Name]!!
        val otherMonkeysNumber =
            if (monkey1.name == trailToHuman.first()) monkey2.calculateValue(monkeys) else monkey1.calculateValue(
                monkeys
            )
        return otherMonkeysNumber
    }

    private fun parseInput(input: String): Monkey {
        val (name, _, number, monkey1, operator, monkey2) = regex.matchEntire(input)!!.destructured
        return if (number.toIntOrNull() != null) {
            Monkey(name, number.toLong())
        } else {
            Monkey(name, 0, Operator.of(operator), monkey1, monkey2)
        }

    }

    private fun getHumanTrail(monkey: Monkey, monkeys: Map<String, Monkey>): List<String>? {
        if (monkey.name == "humn") {
            return listOf()
        }
        return if (monkey.operator == null) null
        else {
            val left = getHumanTrail(monkeys[monkey.monkey1Name!!]!!, monkeys)
            if (left != null) return left + monkey.name
            val right = getHumanTrail(monkeys[monkey.monkey2Name!!]!!, monkeys)
            if (right != null) return right + monkey.name
            null
        }
    }

    private enum class Operator(val symbol: String) {
        ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");

        companion object {
            fun of(symbol: String): Operator {
                return values().first { it.symbol == symbol }
            }
        }
    }

    private data class Monkey(
        val name: String,
        var value: Long = 0,
        var operator: Operator? = null,
        var monkey1Name: String? = null,
        var monkey2Name: String? = null
    ) {
        fun calculateValue(monkeys: Map<String, Monkey>): Long {
            if (value != 0L) {
                return value
            }
            val monkey1 = monkeys[monkey1Name]!!.calculateValue(monkeys)
            val monkey2 = monkeys[monkey2Name]!!.calculateValue(monkeys)
            return when (operator) {
                Operator.ADD -> monkey1 + monkey2
                Operator.SUBTRACT -> monkey1 - monkey2
                Operator.MULTIPLY -> monkey1 * monkey2
                Operator.DIVIDE -> if (monkey1.mod(monkey2) == 0L) monkey1 / monkey2 else throw IllegalArgumentException(
                    "Cannot divide $monkey1 by $monkey2"
                )
                else -> throw IllegalStateException("Operator is null")
            }
        }

    }
}