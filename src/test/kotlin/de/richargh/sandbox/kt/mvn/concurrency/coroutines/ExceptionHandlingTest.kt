package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.fail
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MyException(msg: String): RuntimeException(msg)

internal class ExceptionHandlingTest {

    private val poolSize = 10

    private fun runBlockingTest(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> Any) =
        runBlocking<Unit>(context)
            {
                if (launch { block() }.isCancelled) fail("Probably an exception was swallowed.")
            }

    @Test
    fun `exception is thrown in a coroutine scope and can be caught outside`() {
        assertThrows<MyException> {
            runBlockingTest(ForkJoinPool(poolSize).asCoroutineDispatcher()) {
                launch {
                    delay(500)
                    throw MyException("evil")
                }
            }
        }
    }
}

