package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import org.junit.jupiter.api.fail

class CoroutineTest {

    @Test
    fun `test routines`(testReporter: TestReporter) = runBlocking<Unit> {
        testReporter.publishEntry("Start")

        // Start a coroutine
        launch {
            delay(1_000)
            testReporter.publishEntry("Hello")
        }

        delay(2_000) // wait for 2 seconds
        testReporter.publishEntry("Stop")
    }

    @Test
    fun `trying to cancel the job before fail is printed`(testReporter: TestReporter) = runBlocking<Unit> {
        launch {
            // arrange
            val job = coroutineContext[Job]

            // act
            job?.cancel()

            // assert
            fail("this should not happen, because we did cancel the job")
        }
    }
}