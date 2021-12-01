import dev.manka.advent_of_code.days.Day
import dev.manka.advent_of_code.days.Days
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.measureTimedValue

@ExperimentalTime
fun main(args: Array<String>) {
    hello()
    val dayNumber = if (args.isEmpty()) {
        val input = Scanner(System.`in`)
        input.nextInt()
    } else {
        args[0].toInt()
    }
    val days = Days()
    val day = days.getDay(dayNumber)
    println("running ${day.title} for day ${day.day}...")
    performDay(day)
}

private fun hello() {
    println(asciiGreetings)
    println("What day you want to run?")
}

@ExperimentalTime
private fun performDay(day: Day) {
    val partOne = measureTimedValue { day.partOne() }
    val partTwo = measureTimedValue { day.partTwo() }
    println(" 🎉 Part 1: ${partOne.value}".padEnd(20, ' ') + "(${partOne.duration})")
    println(" 🎆 Part 2: ${partTwo.value}".padEnd(20, ' ') + "(${partTwo.duration})")
}

private val asciiGreetings: String = """
    ||::|:||   .--------,
    |:||:|:|   |_______ /        .-.
    ||::|:|| ."`  ___  `".    {\('v')/}
    \\\/\///:  .'`   `'.  ;____`(   )'____
     \====/ './  o   o  \|~     ^" "^   //
      \\//   |   ())) .  |    HELLO!    \
       ||     \ `.__.'  /|   ADVENT OF  //
       ||   _{``-.___.-'\|   CODE 2021   \
       || _." `-.____.-'`|    ___       //
       ||`        __ \   |___/   \_______\
     ."||        (__) \    \|     /
    /   `\/       __   vvvvv'\___/
    |     |      (__)        |
     \___/\                 /
       ||  |     .___.     |
       ||  |       |       |
       ||.-'       |       '-.
       ||          |          )
       ||----------'---------'
 
""".trimIndent()

