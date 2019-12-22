package de.richargh.sandbox.kt.mvn.concurrency.coroutines.coldflow

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter

class FlowTest {
    @Test
    fun `can collect data from flow`(testReporter: TestReporter) {
        // arrange, notice we need no coroutine code here
        val greeting = flow {
            testReporter.publishEntry("[${Thread.currentThread().name}] Emitting Hello")
            emit("Hello")
            testReporter.publishEntry("[${Thread.currentThread().name}] Emitting World")
            emit("World")
        }

        // act, we need a coroutine to collect a flow, because a flow can suspend
        val result = mutableListOf<String>()
        testReporter.publishEntry("[${Thread.currentThread().name}] About to collect")
        runBlocking {
            greeting.collect { result += it }
        }

        // assert
        assertThat(result).containsExactly("Hello", "World")
    }

    @Test
    fun `can create flow in a different context than where we collect`(testReporter: TestReporter) {
        // arrange, notice we need no coroutine code here
        val greeting = flow {
            // expensive computation simulation
            Thread.sleep(100)
            testReporter.publishEntry("[${Thread.currentThread().name}] Emitting Hello")
            emit("Hello")

            Thread.sleep(100)
            testReporter.publishEntry("[${Thread.currentThread().name}] Emitting Hello")
            emit("World")
        }.flowOn(Dispatchers.Default)

        // act, we need a coroutine to collect a flow, because a flow can suspend
        val result = mutableListOf<String>()
        runBlocking {
            testReporter.publishEntry("[${Thread.currentThread().name}] Collecting")
            greeting.collect { result += it }
        }

        // assert
        assertThat(result).containsExactly("Hello", "World")
    }
}