package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter

class CoroutineTest {

    @Test
    fun `test routines`(testReporter: TestReporter) = runBlocking {
        testReporter.publishEntry("Start")

        // Start a coroutine
        launch {
            delay(1_000)
            testReporter.publishEntry("Hello")
        }

        delay(2_000) // wait for 2 seconds
        testReporter.publishEntry("Stop")
    }
}