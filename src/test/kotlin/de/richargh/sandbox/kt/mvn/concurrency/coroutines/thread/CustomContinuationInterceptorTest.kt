package de.richargh.sandbox.kt.mvn.concurrency.coroutines.thread

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentLinkedDeque
import kotlin.coroutines.Continuation
import kotlin.coroutines.ContinuationInterceptor
import kotlin.coroutines.CoroutineContext

class CustomContinuationInterceptorTest {

    @Test
    fun `can retrieve coroutine name from the context`(){
        // arrange
        val loggingInterceptor =
                LoggingContinuationInterceptor()

        // act
        testBlocking(loggingInterceptor + CoroutineName("block")) {
            launch(CoroutineName("launch")) {
                /* computation that does nothing */
                val i = 0 + 1
                delay(50)
            }
        }

        // assert
        assertThat(loggingInterceptor.pastContinuations).containsExactly("block", "block", "launch")
    }
}


private class LoggingContinuationInterceptor(): ContinuationInterceptor {

    val pastContinuations = ConcurrentLinkedDeque<String>()

    override val key = Key

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        // You could also handle ThreadLocal variables here if the framework you use does not support coroutines
        pastContinuations += continuation.context[CoroutineName]?.name ?: continuation.toString()
        return continuation
    }

    companion object Key : CoroutineContext.Key<ContinuationInterceptor>
}