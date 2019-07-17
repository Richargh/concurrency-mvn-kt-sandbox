package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.*
import java.util.concurrent.ForkJoinPool

fun main() = runBlocking(ForkJoinPool(10).asCoroutineDispatcher()) { // this: CoroutineScope
    launch {
        delay(2000L)
        println("3 Task from runBlocking")
    }

    coroutineScope { // Creates a coroutine scope
        launch {
            println("1 Task from nested launch a")
            delay(3000L)
            println("4 Task from nested launch b")
        }

        delay(1000L)
        println("2 Task from coroutine scope") // This line will be printed before the nested launch
    }

    println("5 Coroutine scope is over") // This line is not printed until the nested launch completes
}