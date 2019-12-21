package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import org.junit.jupiter.api.fail

class CoroutineTest {

    @Test
    fun `trying to cancel the job before fail is printed`(testReporter: TestReporter) = testBlocking {
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

