package de.richargh.sandbox.kt.mvn.concurrency.coroutines.thread

import kotlinx.coroutines.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class CoroutinePerThreadTest {

    @Test
    fun `in a single-thread environment a coroutine always resumes on the same thread`() {
        // arrange
        val threadNamesPerCoroutine = ConcurrentHashMap<String, List<String>>()

        // act
        runBlocking(Executors.newSingleThreadExecutor().asCoroutineDispatcher()) {
            repeat(1) { num ->
                launch {
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                    delay(100)
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                    yield()
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                }
                launch {
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                    yield()
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                    delay(200)
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                }
            }
        }

        // assert
        assertThat(threadNamesPerCoroutine.values)
                .filteredOn { it.size > 1 }.isEmpty()
    }

    @Test
    fun `in a multi-thread environment a coroutine does not necessarily resume on the same thread`() {
        // arrange
        val threadNamesPerCoroutine = ConcurrentHashMap<String, List<String>>()

        // act
        runBlocking(Executors.newFixedThreadPool(10).asCoroutineDispatcher()) {
            repeat(1) { num ->
                launch(CoroutineName("CoroutineA")) {
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                    delay(100)
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                    yield()
                    threadNamesPerCoroutine.mergeElement("A$num", Thread.currentThread().name)
                }
                launch(CoroutineName("CoroutineB")) {
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                    yield()
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                    delay(200)
                    threadNamesPerCoroutine.mergeElement("B$num", Thread.currentThread().name)
                }
            }
        }

        // assert
        assertThat(threadNamesPerCoroutine.values)
                .filteredOn { it.size > 1 }
                .size().isGreaterThan(0)
    }

    private fun <K, V> ConcurrentHashMap<K, List<V>>.mergeElement(key: K, value: V) =
            merge(key, listOf(value)) { oldList, newList ->
                if (oldList == newList)
                    oldList
                else
                    oldList + newList
            }
}