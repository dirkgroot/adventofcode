package util

import kotlin.time.measureTimedValue

typealias Solution<R> = (String) -> R

infix fun <R> Solution<R>.invokedWith(input: String): R =
    measureTimedValue { invoke(input) }.let {
        println("Time:     ${it.duration}")
        println("Solution: ${it.value}")
        it.value
    }
