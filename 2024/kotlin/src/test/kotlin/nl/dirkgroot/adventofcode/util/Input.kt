package nl.dirkgroot.adventofcode.util

import java.io.BufferedReader
import kotlin.time.measureTimedValue

fun input(day: Int): String =
    measureTimedValue { readFromClassPath(day).readText() }
        .let {
            println("Day $day")
            println("I/O:      ${it.duration}")
            it.value
        }

private fun readFromClassPath(day: Int): BufferedReader =
    object {}.javaClass
        .getResourceAsStream("/inputs/${java.lang.String.format("%02d", day)}.txt")
        ?.bufferedReader()
        ?: throw IllegalStateException("Input for day $day not available!")
