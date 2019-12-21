package de.richargh.sandbox.kt.mvn.concurrency.coroutines.job

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class SupervisionTest {

    @Test
    fun `a supervisorjob does not propagate cancellations downwards`() =
            testBlocking {
                val firstChildFail = "First child fail"
                val secondChildWin = "Second child Win"
                val supervisorJob = SupervisorJob()
                // arrange
                val data = mutableListOf<String>()
                with(CoroutineScope(coroutineContext + supervisorJob)) {
                    // first child exception is ignored for this example (don't do this in practice!)
                    val firstChild = launch(CoroutineExceptionHandler { _, _ -> }) {
                        data += firstChildFail
                        throw AssertionError("First child is cancelled")
                    }
                    // launch the second child
                    val secondChild = launch {
                        firstChild.join()
                        data += secondChildWin
                    }

                    // act
                    firstChild.join()
                    secondChild.join()
                }

                // assert
                assertThat(data).containsExactly(firstChildFail, secondChildWin)
            }

    @Test
    fun `a regular job propagates cancellations downwards`() =
            testBlocking(
                    CoroutineExceptionHandler { _, _ -> }) {
                // first child exception is ignored for this example (don't do this in practice!)

                val firstChildFail = "First child fail"
                val regularJob = Job()
                // arrange
                val data = mutableListOf<String>()
                with(CoroutineScope(coroutineContext + regularJob)) {
                    val firstChild = launch {
                        data += firstChildFail
                        throw AssertionError("First child is cancelled")
                    }
                    // launch the second child
                    val secondChild = launch {
                        firstChild.join()
                        data += "Second child won't happen"
                    }

                    // act
                    firstChild.join()
                    secondChild.join()
                }

                // assert
                assertThat(data).containsExactly(firstChildFail)
            }
}