package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.CoroutineContext

class FiboTest {

    @Test
    fun `Fibo foo something test`() = runBlocking<Unit> {
        // arrange
        val spyPublisher = SpyPublisher()
        val fibo = SuspendingFibo(spyPublisher)

        // act
        fibo.doSth()

        // assert
        assertThat(spyPublisher.publishedMessages).containsExactly("doSth", "doRegular")
    }

    @ObsoleteCoroutinesApi
    @ExperimentalCoroutinesApi
    @Test
    fun `start lots of stuff `() = runBlocking<Unit> {
        launch { // context of the parent, main runBlocking coroutine
            println("(1)L: main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Unconfined) { // not confined -- will work with main thread
            println("(2)L: Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // will get dispatched to DefaultDispatcher
            println("(3)L: Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnThread")) { // will get its own new thread
            println("(4)L: newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }

        async {
            println("(5)A: main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }.await()

        withContext(Dispatchers.Default) {
            println("(6)C: main runBlocking      : I'm working in thread ${Thread.currentThread().name}")
        }

        println("My job is ${coroutineContext[Job]}")

    }

    @Test
    fun `runblocking waits until all async tasks are done`() = runBlocking<Unit> {
        // arrange
        val spyPublisher = SpyPublisher()

        // act
        runBlocking {
            async {
                delay(500)
                spyPublisher.publish("2")
            }
            async {
                delay(200)
                spyPublisher.publish("1")
            }
        }
        spyPublisher.publish("3")

        // assert
        assertThat(spyPublisher.publishedMessages).containsExactly("1", "2", "3")
    }

    @Test
    fun `creating a custom dispatcher`() = runBlocking<Unit> {
        // arrange
        val spyPublisher = SpyPublisher()

        // act
        async(ForkJoinPool(10).asCoroutineDispatcher()) { println("Thread ${Thread.currentThread().name}") }

        // assert
        assertThat(spyPublisher.publishedMessages).containsExactly("1", "2", "3")
    }
}

class MyCoroutineContext: CoroutineContext {
    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <E: CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
