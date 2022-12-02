package dev.manka.advent_of_code.days
import org.reflections.Reflections

class Days {

    fun getDay(day:Int,year: Int) : Day{
        val reflections = Reflections("dev.manka.advent_of_code.days.y${year}")
        val objects = reflections.getSubTypesOf(Day::class.java)
        return objects.first{ it.simpleName.getDigits()==day}
            .constructors[0].newInstance() as Day
    }
    private fun String.getDigits():Int{
        return this.filter { it.isDigit() }.toInt()
    }
}