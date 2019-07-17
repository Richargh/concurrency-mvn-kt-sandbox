package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("Result = " + sum(1, 2, 3))
}

fun sum(i: Int, j: Int, k: Int): Int {
    val start = System.currentTimeMillis()

    var result: Int = 0
    runBlocking {
        // note that some of these three coroutines are lazy, hence not computed before asked with await()
        val jobA = async(start = CoroutineStart.DEFAULT) { identity(start, i) }
        val jobB = async(start = CoroutineStart.LAZY)    { identity(start, j) }
        val jobC = async(start = CoroutineStart.LAZY)    { identity(start, k) }

        delay(1000)
        println("${System.currentTimeMillis() - start} Now asking for result for $i")
        result = jobA.await()

        delay(1000)
        println("${System.currentTimeMillis() - start} Now asking for result for $j")
        result += jobB.await()

        delay(1000)
        println("${System.currentTimeMillis() - start} Now asking for result for $k")
        result += jobC.await()
    }

    return result
}

suspend fun identity(start: Long, i: Int): Int {
    delay(100)

    println("${System.currentTimeMillis() - start} Computing and returning now value for identity for $i")
    return i
}
