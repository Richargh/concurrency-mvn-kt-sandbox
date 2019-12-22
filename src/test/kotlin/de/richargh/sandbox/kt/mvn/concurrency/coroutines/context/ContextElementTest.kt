package de.richargh.sandbox.kt.mvn.concurrency.coroutines.context

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

internal class ContextElementTest {

    @Test
    fun `can retrieve coroutine name from the context`() = testBlocking {
        // arrange
        val expected = "echo"

        // act
        val value = async(CoroutineName(expected)) { coroutineContext[CoroutineName]?.name }

        // assert
        assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `can retrieve job from the context`() = testBlocking {
        // arrange
        val coroutineName = "echo"

        // act
        val value = async(CoroutineName(coroutineName)) { coroutineContext[Job].toString() }

        // assert
        assertThat(value.await()).startsWith("\"$coroutineName")
    }

    @Test
    fun `cannot retrieve exception handler from the context if it wasn't placed there`() = testBlocking {
        // arrange
        val coroutineName = "echo"

        // act
        val value = async(CoroutineName(coroutineName)) { coroutineContext[CoroutineExceptionHandler] }

        // assert
        assertThat(value.await()).isNull()
    }

    @Test
    fun `can retrieve exception handler from the context`() = testBlocking {
        // arrange
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception with suppressed ${exception.suppressed?.contentToString()}")
        }

        // act
        val value = async(handler) { coroutineContext[CoroutineExceptionHandler] }

        // assert
        assertThat(value.await()).isNotNull()
    }

}