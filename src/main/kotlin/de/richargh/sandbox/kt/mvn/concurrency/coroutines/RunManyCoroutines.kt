package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.ForkJoinPool

class RunManyCoroutines(
    private val threadPoolSize: Int = 3,
    private val numCoRoutines: Int = 100,
    private val delayInMS: Long = 100L,
    private val isBlocking: Boolean = false)
{
    fun startManyCoroutines(): Long {
        val time = System.currentTimeMillis()

        runBlocking(ForkJoinPool(threadPoolSize).asCoroutineDispatcher()) {
            repeat(numCoRoutines) {
                val job = launch {
                    if (isBlocking) {
                        Thread.sleep(delayInMS)     // blocks the thread
                    } else {
                        delay(delayInMS)            // does not block the thread, so way faster
                    }

                    println(Thread.currentThread().name + " runs " + this.toString())
                }
            }
        }

        val duration = System.currentTimeMillis() - time
        println("In total this took ${duration} ms")

        return duration
    }
}
