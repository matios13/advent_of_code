package dev.manka.advent_of_code.days.commons

fun <E> ArrayDeque<E>.rotate() = addLast(removeFirst())