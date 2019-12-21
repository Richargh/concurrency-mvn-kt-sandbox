package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors

class CustomDispatcherTest {

    @Test
    fun `can create a custom dispatcher`(testReporter: TestReporter) {
        // arrange
        val messages = (1..3).map { "$it" }

        // act
        val result = ConcurrentLinkedQueue<String>()
        testBlocking(Executors.newFixedThreadPool(10).asCoroutineDispatcher()) {
            messages.forEach { message ->
                launch {
                    testReporter.publishEntry("Thread ${Thread.currentThread().name}")
                    result += message
                }
            }
        }

        // assert
        assertThat(result).containsExactlyInAnyOrder(*messages.toTypedArray())
    }
}