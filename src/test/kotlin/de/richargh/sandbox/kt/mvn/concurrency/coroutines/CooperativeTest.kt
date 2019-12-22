package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter

/**
 * Code here is taken from the official docs: https://kotlinlang.org/docs/reference/coroutines/cancellation-and-timeouts.html
 */
class CooperativeTest {

    @Test
    fun `a coroutine that is not cooperative cannot be canceled`(testReporter: TestReporter) {
        // arrange

        // act
        var i = 0
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                while (i < 5) { // NOT-cooperative
                    // computation loop, imagine it wastes a lot of CPU and does not stop
                    if (System.currentTimeMillis() > nextPrintTime) {
                        i++
                        testReporter.publishEntry("job: I'm sleeping $i ...")
                        nextPrintTime += 100L
                    }
                }
            }
            delay(150)
            job.cancel()
        }

        // assert
        assertThat(i).isEqualTo(5)
    }

    @Test
    fun `a coroutine that is cooperative can be canceled`(testReporter: TestReporter) {
        // arrange

        // act
        var i = 0
        runBlocking {
            val startTime = System.currentTimeMillis()
            val job = launch(Dispatchers.Default) {
                var nextPrintTime = startTime
                while (isActive) { // COoperative, alternative is yield()
                    // computation loop, imagine it wastes a lot of CPU and does not stop
                    if (System.currentTimeMillis() > nextPrintTime) {
                        i++
                        testReporter.publishEntry("job: I'm sleeping $i ...")
                        nextPrintTime += 100L
                    }
                }
            }
            delay(110)
            job.cancel()
        }

        // assert
        assertThat(i).isEqualTo(2)
    }

    @Test
    fun `a coroutine that is cooperative can be cancelled and will also reach finally`(testReporter: TestReporter) {
        // arrange

        // act
        var hasReachedFinally = false
        runBlocking {
            val job = launch(Dispatchers.Default) {
                try {
                    repeat(1000) { i ->
                        testReporter.publishEntry("job: I'm sleeping $i ...")
                        delay(100L)
                    }
                } finally {
                    testReporter.publishEntry("job: I'm running finally")
                    hasReachedFinally = true
                }
            }
            delay(110)
            job.cancel()
        }

        // assert
        assertThat(hasReachedFinally).isTrue()
    }


}