package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import kotlinx.coroutines.*
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ForkJoinPool

internal class SemiBlockingMultiMapTest {

    val initialCapacity = 20

    val aKey = 21
    val otherKey = 42

    val value1 = "asd"
    val value2 = "foo"
    val value3 = "bla"

    val smallDelay = 20L

    @Test
    fun `adding elements at roughly the same time does not interfere with each other`(testReporter: TestReporter) = runBlockingTest(ForkJoinPool(10).asCoroutineDispatcher()) {
        val elementCount = 50
        // arrange
        val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

        // act
        (1..elementCount).forEach {
            launch {
                delay(it.toLong())
                val collection = concurrentMultiMap[aKey]
                collection.add(it.toString())
                testReporter.publishEntry("[${Thread.currentThread().name}] Just added value $it with a count of ")
            }
        }
        delay(500)

        // assert
        Assertions.assertThat(concurrentMultiMap[aKey]).hasSize(elementCount)
    }


    @Test
    fun `adding and removing elements at roughly the same time does not interfere with each other`(testReporter: TestReporter) = runBlockingTest(ForkJoinPool(10).asCoroutineDispatcher()) {
        val addCount = 50
        val deleteCount = addCount / 2
        // arrange
        val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

        // act
        coroutineScope {
            (1..addCount).forEach {
                launch {
                    delay(it.toLong())
                    concurrentMultiMap[aKey].add(it.toString())
                    testReporter.publishEntry("[${Thread.currentThread().name}] Just added value $it")
                    delay(it.toLong())
                    concurrentMultiMap[aKey].remove(it.toString())
                    testReporter.publishEntry("[${Thread.currentThread().name}] Just removed value $it")
                }
            }
        }

        // assert
        Assertions.assertThat(concurrentMultiMap[aKey]).hasSize(0)
    }

    @Test
    fun `accessing a non-existing element results in an empty list`() {
        // arrange
        val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

        // act
        val collection = concurrentMultiMap[aKey]

        // assert
        Assertions.assertThat(collection).isEmpty()
    }

    @Test
    fun `values contain all items independendent of their key`() {
        // arrange
        val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

        // act
        concurrentMultiMap[aKey].add(value1)
        concurrentMultiMap[aKey].add(value2)
        concurrentMultiMap[otherKey].add(value3)

        // assert
        Assertions.assertThat(concurrentMultiMap.values()).containsExactlyInAnyOrder(value1, value2, value3)
    }

    @Test
    fun `finds all keys`() {
        // arrange
        val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

        // act
        concurrentMultiMap[aKey].add(value1)
        concurrentMultiMap[aKey].add(value2)
        concurrentMultiMap[otherKey].add(value3)

        // assert
        Assertions.assertThat(concurrentMultiMap.keys()).containsExactlyInAnyOrder(aKey, otherKey)
    }

    @Nested
    inner class Add {

        @Test
        fun `after adding an element we can find it again`() {
            // arrange
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

            // act
            concurrentMultiMap[aKey].add(value1)

            // assert
            Assertions.assertThat(concurrentMultiMap[aKey]).containsExactly(value1)
        }

        @Test
        fun `after adding multiple elements we can find it again`() {
            // arrange
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

            // act
            concurrentMultiMap[aKey].add(value1)
            concurrentMultiMap[aKey].add(value2)

            // assert
            Assertions.assertThat(concurrentMultiMap[aKey]).containsExactly(value1, value2)
        }

        @Test
        fun `elements with different keys do not influence each others collections`() {
            // arrange
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

            // act
            concurrentMultiMap[aKey].add(value1)
            concurrentMultiMap[otherKey].add(value2)

            // assert
            Assertions.assertThat(concurrentMultiMap[aKey]).containsExactly(value1)
            Assertions.assertThat(concurrentMultiMap[otherKey]).containsExactly(value2)
        }

        @Test
        fun `when your key is null, the map does not store anyhting`() {
            // arrange
            val key: Int? = null
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)

            // act
            concurrentMultiMap[key].add(value1)

            // assert
            Assertions.assertThat(concurrentMultiMap[key]).isEmpty()
        }
    }

    @Nested
    inner class Remove {

        @Test
        fun `we can clear all added elements as well`() {
            // arrange
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)
            concurrentMultiMap[aKey].add(value1)

            // act
            concurrentMultiMap[aKey].clear()

            // assert
            Assertions.assertThat(concurrentMultiMap[aKey]).isEmpty()
        }

        @Test
        fun `we can remove individual elements if we want`() {
            // arrange
            val concurrentMultiMap = SemiBlockingMultiMap<Int, String>(initialCapacity)
            concurrentMultiMap[aKey].add(value1)
            concurrentMultiMap[aKey].add(value2)

            // act
            concurrentMultiMap[aKey].remove(value1)

            // assert
            Assertions.assertThat(concurrentMultiMap[aKey]).containsExactly(value2)
        }
    }
}