package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

internal class RunManyCoroutinesTest {

    @Test
    fun `running many coroutines with blocking sleep takes X times longer than with non-blocking delay`() {
        // arrange
        val numCoRoutines = 10
        val threadPoolSize = 3
        val delayInMS = 100L
        val runManyBlockingCoroutines = RunManyCoroutines(threadPoolSize, numCoRoutines,    delayInMS, isBlocking = true)
        val runManyNonBlockingCoroutines = RunManyCoroutines(threadPoolSize, numCoRoutines, delayInMS, isBlocking = false)

        val offset = 30 // fixed offset for startup, in ms
        val min_factor = 0.7
        val max_factor = 1.3

        // act
        val durationBlocking = runManyBlockingCoroutines.startManyCoroutines()
        val durationNonBlocking = runManyNonBlockingCoroutines.startManyCoroutines()

        // assert
        assertThat(durationBlocking > durationNonBlocking)

        val expectedDurationBlocking: Long = offset +
                 Math.ceil(numCoRoutines.toDouble() / threadPoolSize.toDouble()).toLong() * delayInMS
        val expectedDurationNonBlocking = offset +
                 1 * delayInMS

        assertTrue(durationBlocking * min_factor < expectedDurationBlocking)
        assertTrue(expectedDurationBlocking < durationBlocking * max_factor)

        assertTrue(durationNonBlocking * min_factor < expectedDurationNonBlocking)
        assertTrue(expectedDurationNonBlocking < durationNonBlocking * max_factor)
    }
}