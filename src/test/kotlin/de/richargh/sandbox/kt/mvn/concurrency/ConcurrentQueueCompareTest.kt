package de.richargh.sandbox.kt.mvn.concurrency

import org.apache.commons.lang3.time.StopWatch
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestReporter
import java.util.concurrent.ConcurrentLinkedQueue

class ConcurrentQueueCompareTest {

    @Test
    fun `Using queue-size is not a constant time operation and costs a lot of performance`(testReporter: TestReporter) {
        // arrange
        val count = 100_000
        val maxSize = count / 10
        val stopWatch = StopWatch()
        val queue = ConcurrentLinkedQueue<String>()

        // act
        stopWatch.start()
        (1..count).toList().parallelStream().forEach {
            queue.add(it.toString())
            if (queue.size > maxSize) {
                queue.poll()
            }
        }
        stopWatch.stop()

        // assert
        testReporter.publishEntry("It took ${stopWatch.time} ms")
        assertThat(stopWatch.time).isGreaterThan(2000)
    }


}