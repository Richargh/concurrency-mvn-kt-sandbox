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

    @Test
    fun `can retrieve custom element from the context`() = testBlocking {
        // arrange
        val expected = 42

        // act
        val value = async(CustomNumberElement(
                expected)) { coroutineContext[CustomNumberElement]?.number }

        // assert
        assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `can retrieve custom element from the context even in children`() = testBlocking {
        // arrange
        val expected = 42

        // act
        val value: Int? = async(
                CustomNumberElement(expected)) {
            async {
                coroutineContext[CustomNumberElement]?.number
            }.await()
        }.await()

        // assert
        assertThat(value).isEqualTo(expected)
    }

    @Test
    fun `can combine and retrieve custom elements from the context`() = testBlocking {
        // arrange
        val name = "echo"
        val number = 42
        val expected = "${name}${number}"

        // act
        val value = async(CoroutineName(name) + CustomNumberElement(
                number)) {
            coroutineContext[CoroutineName]?.name + coroutineContext[CustomNumberElement]?.number
        }

        // assert
        assertThat(value.await()).isEqualTo(expected)
    }

}

private class CustomNumberElement(val number: Int): AbstractCoroutineContextElement(
        Key), CoroutineContext.Element {

    override val key = Key

    companion object Key: CoroutineContext.Key<CustomNumberElement>
}