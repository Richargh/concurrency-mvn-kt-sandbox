package de.richargh.sandbox.kt.mvn.concurrency.coroutines.thread

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import java.util.concurrent.Executors

/**
 * Please be aware that ThreadLocal values can be dangerous when you have long-running applications that use thread pools. That can lead to ThreadLocal values never being garbage collected because their thread never is.
 */
class ThreadLocalTest {

    @Test
    fun `a ThreadLocal value set before creating the Coroutine is available`() = testBlocking {
        // arrange
        val myThreadLocal = ThreadLocal<MyData?>()
        val expected = MyData("will be there")

        // act
        myThreadLocal.set(expected)
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement()) {
            delay(100)
            withContext(executor) {
                myThreadLocal.get()
            }
        }

        // assert
        Assertions.assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `a ThreadLocal value set after creating the Coroutine wont be available`() = testBlocking {
        // arrange
        val myThreadLocal = ThreadLocal<MyData?>()

        // act
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement()) {
            delay(100)
            withContext(executor) {
                myThreadLocal.get()
            }
        }
        myThreadLocal.set(MyData("wontbethere"))

        // assert
        Assertions.assertThat(value.await()).isNull()
    }

    @Test
    fun `a ThreadLocal value can be overriden in the context`() = testBlocking {
        // arrange
        val myThreadLocal = ThreadLocal<MyData?>()
        val expected = MyData("will be there")

        // act
        myThreadLocal.set(MyData("wont be there"))
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement(expected)) {
            delay(100)
            withContext(executor) {
                myThreadLocal.get()
            }
        }

        // assert
        Assertions.assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `a ThreadLocal value can be mutated when changing context`() = testBlocking {
        // arrange
        val expected = MyData("will be there")
        val myThreadLocal = ThreadLocal<MyData?>()

        // act
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement()) {
            withContext(executor + myThreadLocal.asContextElement(expected)) {
                delay(100)
                myThreadLocal.get()
            }
        }

        // assert
        Assertions.assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `a mutated ThreadLocal value is only present in the new context, not in the parent context`() = testBlocking {
        // arrange
        val expected = MyData("will be there")
        val myThreadLocal = ThreadLocal<MyData?>()

        // act
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement(expected)) {
            withContext(executor + myThreadLocal.asContextElement(MyData("wont be there"))) {
                delay(100)
            }
            myThreadLocal.get()
        }

        // assert
        Assertions.assertThat(value.await()).isEqualTo(expected)
    }

    @Test
    fun `a ThreadLocal cannot be directly mutated inside a coroutine`() = testBlocking {
        // arrange
        val myThreadLocal = ThreadLocal<MyData?>()

        // act
        val value: Deferred<MyData?> = async(Dispatchers.Default + myThreadLocal.asContextElement()) {
            delay(100)
            myThreadLocal.set(MyData("wont be there"))
            delay(100)
            myThreadLocal.get()
        }

        // assert
        Assertions.assertThat(value.await()).isNull()
    }

    private val executor = Executors.newFixedThreadPool(1).asCoroutineDispatcher()

    private data class MyData(val someName: String)
}