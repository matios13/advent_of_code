package dev.manka.advent_of_code.days
import org.reflections.Reflections

class Days {
    private var reflections = Reflections("dev.manka.advent_of_code.days.y2021")
    fun getDay(day:Int) : Day{
        val objects = reflections.getSubTypesOf(Day::class.java)
        return objects.first{ it.simpleName.getDigits()==day}.constructors[0].newInstance() as Day
    }
    private fun String.getDigits():Int{
        return this.filter { it.isDigit() }.toInt()
    }
}