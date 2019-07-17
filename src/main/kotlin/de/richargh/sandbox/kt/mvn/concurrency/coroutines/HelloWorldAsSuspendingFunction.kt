package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.*

fun main() = runBlocking {
    val job = coroutineScope {
        launch { doHello() }
        launch { doWorld() }

        println("This is ... ")
    }

    println("!")
}

suspend fun doHello() {
    delay(100L)
    print("Hello ")
    delay(100L)
}
suspend fun doWorld() {
    delay(500L)
    print("World")
    delay(500L)
}