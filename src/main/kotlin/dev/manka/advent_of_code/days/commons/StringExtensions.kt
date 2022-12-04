package dev.manka.advent_of_code.days.commons

fun String.reverseCase(): String = map { if (it.isUpperCase()) it.lowercaseChar() else it.uppercaseChar() }.joinToString("")

