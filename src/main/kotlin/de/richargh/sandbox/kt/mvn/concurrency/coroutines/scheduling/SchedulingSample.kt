package de.richargh.sandbox.kt.mvn.concurrency.coroutines.scheduling

import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    getTicks(delayMillis = 1_000L, initialDelayMillis = 2_000L)
}

suspend fun getTicks(delayMillis: Long, initialDelayMillis: Long) {
    val tickerChannel = ticker(delayMillis, initialDelayMillis)

    val start = System.currentTimeMillis()
    runBlocking {
        repeat(10) {
            tickerChannel.receive()
            println(Thread.currentThread().name + " gets tick at ${System.currentTimeMillis() - start}")
        }
    }
}
