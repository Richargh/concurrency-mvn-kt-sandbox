package de.richargh.sandbox.kt.mvn.concurrency.coroutines.exception

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ExceptionHandlingTest {

    @Test
    fun `exception is thrown in a launched scope and can be caught outside`() {
        // arrange

        // act
        val act = {
            testBlocking(Dispatchers.Default) {
                launch {
                    delay(500)
                    throw MyException("evil")
                }
            }
        }

        // assert
        assertThrows<MyException>(act)
    }

    @Test
    fun `exception is thrown in an async scope and can be caught outside`() {
        // arrange

        // act
        val act = {
            testBlocking(Dispatchers.Default) {
                async {
                    delay(500)
                    throw MyException("evil")
                }
            }
        }

        assertThrows<MyException>(act)
    }

    @Test
    fun `exception is thrown in an async scope and cannot be caught with handler`() {
        // arrange
        val handler = CoroutineExceptionHandler { _, exception ->
            println("Caught $exception with suppressed ${exception.suppressed?.contentToString()}")
        }

        // act
        val act = {
            testBlocking(handler) {
                async {
                    delay(500)
                    throw MyException(
                            "evil")
                    "won't be returned"
                }.await()
            }
        }

        // assert
        assertThrows<MyException>(act)
    }

    private class MyException(msg: String): RuntimeException(msg)
}

