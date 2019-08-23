package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Publisher
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import org.junit.jupiter.api.Test

abstract class PublisherContract(private val publisher: Publisher) {

    @Test
    fun `if I don't subscribe to an event I don't get notified`() = runBlockingTest {
        // arrange

        // act
//        publisher.subscribe<> {  }

        // assert

    }
}