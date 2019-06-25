package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

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
}