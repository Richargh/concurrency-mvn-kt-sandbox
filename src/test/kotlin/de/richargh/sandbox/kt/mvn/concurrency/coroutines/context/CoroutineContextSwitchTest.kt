package de.richargh.sandbox.kt.mvn.concurrency.coroutines.context

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter

class CoroutineContextSwitchTest {

    @Test
    fun `can switch between different coroutine contexts`(testReporter: TestReporter) = testBlocking {
        // arrange
        val worker = Worker(this, testReporter)

        // act
        val result = worker.combineContextWork()

        // assert
        assertThat(result).containsExactly("io", "parent", "default", "worker")
    }

}

private class Worker(private val scope: CoroutineScope, private val testReporter: TestReporter) {

    suspend fun combineContextWork(): List<String> = coroutineScope {
        testReporter.publishEntry("${Thread.currentThread().name} worker Context")

        val ioContextResult: String = withContext(Dispatchers.IO){
            testReporter.publishEntry("${Thread.currentThread().name} IO context")
            "io"
        }

        val parentContextResult: String = withContext(scope.coroutineContext) {
            testReporter.publishEntry("${Thread.currentThread().name} parent context")

            "parent"
        }

        val defaultContextResult: String = withContext(Dispatchers.Default){
            testReporter.publishEntry("${Thread.currentThread().name} Default context")
            "default"
        }

        listOf(ioContextResult, parentContextResult, defaultContextResult, "worker")
    }
}
