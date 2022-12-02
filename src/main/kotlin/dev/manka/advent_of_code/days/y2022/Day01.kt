package dev.manka.advent_of_code.days.y2022

import dev.manka.advent_of_code.days.Day

class Day01 : Day(1,"Sonar Sweep",2022){
    override fun partOne(): String {
        return extractElvesWithCalories(inputList).max().toString()
    }

    override fun partTwo(): String {
        return extractElvesWithCalories(inputList).sortedDescending().take(3).sum().toString()
    }
    private fun extractElvesWithCalories(calories: List<String>): List<Int>{
        val elvesWithCalories = mutableListOf<Int>()
        var currentCalories = 0
        calories.forEach {
            if(it.isEmpty()){
                elvesWithCalories.add(currentCalories)
                currentCalories = 0
            }else{
                currentCalories += it.toInt()
            }
        }
        return elvesWithCalories
    }


}