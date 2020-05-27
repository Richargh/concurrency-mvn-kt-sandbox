package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.atomic.AtomicInteger

/**
 * This test uses a ConcurrentLinkedQueue to simulate an evicting queue.
 * And evicting queue has an upper bound and cannot have more than maxSize elements.
 * Unfortunately the ConcurrentLinkedQueue.size method has a lot of overhead.
 */
class EvictingQueuePerformanceCompareTest {

    @Test
    fun `Using queue-size is not a constant time operation and costs a lot of performance`(testReporter: TestReporter) {
        // arrange
        val count = 100_000
        val maxSize = count / 10
        val stopWatch = StopWatch()
        val queue = ConcurrentLinkedQueue<String>()
        val threadNames = ConcurrentHashMap<String, Unit>()

        // act
        stopWatch.start()
        (1..count).toList().parallelStream().forEach {
            threadNames[Thread.currentThread().name] = Unit
            queue.add(it.toString())
            if (queue.size > maxSize) {
                queue.poll()
            }
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        assertThat(queue.size).isEqualTo(maxSize)
        assertThat(stopWatch.time).isGreaterThan(2000)
    }

    @Test
    fun `Using a separate integer size leads to the wrong list size but is rather fast`(testReporter: TestReporter) {
        // arrange
        val count = 100_000
        val maxSize = count / 10
        val stopWatch = StopWatch()
        val queue = ConcurrentLinkedQueue<String>()
        val threadNames = ConcurrentHashMap<String, Unit>()

        // act
        var size = 0
        stopWatch.start()
        (1..count).toList().parallelStream().forEach {
            threadNames[Thread.currentThread().name] = Unit
            queue.add(it.toString())
            size++
            if (size > maxSize) {
                queue.poll()
            }
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        assertThat(queue.size).isGreaterThan(maxSize)
        assertThat(stopWatch.time).isLessThan(120)
    }

    @Test
    fun `Using a separate atomicinteger size leads to the right list size and is rather fast`(testReporter: TestReporter) {
        // arrange
        val count = 100_000
        val maxSize = count / 10
        val stopWatch = StopWatch()
        val queue = ConcurrentLinkedQueue<String>()
        val threadNames = ConcurrentHashMap<String, Unit>()

        // act
        val size = AtomicInteger(0)
        stopWatch.start()
        (1..count).toList().parallelStream().forEach {
            threadNames[Thread.currentThread().name] = Unit
            queue.add(it.toString())
            if (size.incrementAndGet() > maxSize) {
                queue.poll()
            }
        }
        stopWatch.stop()

        // report
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        testReporter.publishEntry("We executed on these threads ${threadNames.keys().toList().joinToString(", ")}")

        // assert
        assertThat(queue.size).isEqualTo(maxSize)
        assertThat(stopWatch.time).isLessThan(50)
    }


}