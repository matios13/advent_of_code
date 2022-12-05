package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day
import java.util.Deque
import java.util.LinkedList

class Day05 : Day(5, "Supply Stacks ", 2022) {
    override fun partOne(): String {
        val cratesWithMoves = getCratesWithMoves(inputList)
        cratesWithMoves.moves.forEach { cratesWithMoves.stacks.move(it) }
        return cratesWithMoves.stacks.map { it.first }.joinToString("") { it.toString() }
    }

    override fun partTwo(): String {
        val cratesWithMoves = getCratesWithMoves(inputList)
        cratesWithMoves.moves.forEach { cratesWithMoves.stacks.moveMultiple(it) }
        return cratesWithMoves.stacks.map { it.first }.joinToString("") { it.toString() }
    }


    private fun getCratesWithMoves(rows: List<String>) : CratesWithMoves{

        val stacks = MutableList<Deque<Char>>(9) { LinkedList() }
        rows
            .filter { it.contains("[") }
            .forEach {
                it.chunked(4).mapIndexed() { index, s ->
                    if(s.contains("[")){
                        stacks[index].addLast(s[1])
                    }
                }
            }


        val movesRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")
        val moves = rows
            .filter { it.contains("move") }
            .map { movesRegex.find(it)!!.groupValues }
            .map { Move(it[1].toInt(), it[2].toInt(), it[3].toInt()) }
        return CratesWithMoves(stacks, moves)

    }

    private fun MutableList<Deque<Char>>.move(move: Move) {
        for(i in 0 until move.howMany){
            this[move.to-1].addFirst(this[move.from-1].removeFirst())
        }
    }
    private fun MutableList<Deque<Char>>.moveMultiple(move: Move) {
        val tempStack = LinkedList<Char>()
        for(i in 0 until move.howMany){
            tempStack.addFirst(this[move.from-1].removeFirst())
        }
        tempStack.forEach {
            this[move.to-1].addFirst(it)
        }
    }

    private data class Move(val howMany: Int, val from: Int, val to: Int)
    private data class CratesWithMoves(val stacks: MutableList<Deque<Char>>, val moves: List<Move>)
}