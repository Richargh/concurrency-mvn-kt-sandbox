package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.math.ceil

internal class SleepVsDelayTest {

    @Test
    fun `running many coroutines with blocking sleep takes X times longer than with non-blocking delay`() {
        // arrange
        val numCoRoutines = 10
        val threadPoolSize = 3
        val delayInMS = 100L
        val runManyBlockingCoroutines = RunManyCoroutines(threadPoolSize, numCoRoutines, delayInMS, isBlocking = true)
        val runManyNonBlockingCoroutines =
                RunManyCoroutines(threadPoolSize, numCoRoutines, delayInMS, isBlocking = false)

        val offset = 30 // fixed offset for startup, in ms
        val minFactor = 0.7
        val maxFactor = 1.5

        // act
        val durationBlocking = runManyBlockingCoroutines.startManyCoroutines()
        val durationNonBlocking = runManyNonBlockingCoroutines.startManyCoroutines()

        // assert
        assertThat(durationBlocking > durationNonBlocking)

        val expectedDurationBlocking: Long =
                offset + ceil(numCoRoutines.toDouble() / threadPoolSize.toDouble()).toLong() * delayInMS
        val expectedDurationNonBlocking = offset + 1 * delayInMS

        assertThat(durationBlocking * minFactor).isLessThan(expectedDurationBlocking.toDouble())
        assertThat(expectedDurationBlocking.toDouble()).isLessThan(durationBlocking * maxFactor)

        assertThat(durationNonBlocking * minFactor).isLessThan(expectedDurationNonBlocking.toDouble())
        assertThat(expectedDurationNonBlocking.toDouble()).isLessThan(durationNonBlocking * maxFactor)
    }
}