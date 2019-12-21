package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import de.richargh.sandbox.kt.mvn.concurrency.Publisher
import de.richargh.sandbox.kt.mvn.concurrency.SuspendingFibo
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors.newFixedThreadPool

class StructuredConcurrencyTest {

    @Test
    fun `a coroutine only terminates when all child coroutines are done`() {
        // arrange
        val messages = ConcurrentLinkedQueue<String>()

        // act
        runBlocking {
            launch {
                delay(500)
                messages += "2"
            }
            launch {
                delay(200)
                messages += "1"
            }
        }
        messages += "3"

        // assert
        assertThat(messages).containsExactly("1", "2", "3")
    }
}