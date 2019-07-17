package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import org.junit.jupiter.api.fail

class CoroutineTest {

    @Test
    fun `test routines`(testReporter: TestReporter) = runBlocking<Unit> {
        testReporter.publishEntry("Start")

        // Start a coroutine
        launch {
            delay(1_000)
            testReporter.publishEntry("Hello")
        }

        delay(2_000) // wait for 2 seconds
        testReporter.publishEntry("Stop")
    }

    @Test
    fun `trying to cancel the job before fail is printed`(testReporter: TestReporter) = runBlocking<Unit> {
        launch {
            // arrange
            val job = coroutineContext[Job]

            // act
            job?.cancel()

            // assert
            fail("this should not happen, because we did cancel the job")
        }
    }

    @Test
    fun `blub`(testReporter: TestReporter) = runBlocking<Unit> {
        val foo = Foo(this)
        foo.foo()
        println("${Thread.currentThread().name} Stop")
    }
}

class Foo(private val scope: CoroutineScope) {

    suspend fun foo(): String {
        val blub = withContext(scope.coroutineContext) {
            println("${Thread.currentThread().name} Launch")

        val bar = Bar()
        bar.bar()

        "foo"
        }

        return blub + "bar"
    }
}

class Bar {
    fun bar(){

    }
}