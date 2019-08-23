package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.EventId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.runBlockingTest
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

abstract class NotifierContract(private val notifier: Notifier) {

    @Test
    fun `I don't get notified if I subscribe to an event and no one publishes an event`() = runBlockingTest {
        // arrange

        // act
        notifier.subscribe(
                SomethingHappened::class) {
            fail("I should never be called because no one published")
        }

        // assert by not failing even when waiting
        delay(200)
    }

    @Test
    fun `I get notified if I subscribe to an event and someone publishes that`() = runBlockingTest {
        // arrange
        var wasCalled = false
        notifier.subscribe(
                SomethingHappened::class) {
            wasCalled = true
        }

        // act
        notifier.publish(SomethingHappened())

        // assert by not failing even when waiting
        assertThat(wasCalled).isTrue()
    }

    @Test
    fun `I do not get notified if I subscribe to an event and someone publishes a different event`() = runBlockingTest {
        // arrange
        var wasCalled = false
        notifier.subscribe(
                SomethingHappened::class) {
            wasCalled = true
        }

        // act
        notifier.publish(SomethingElseHappened())

        // assert by not failing even when waiting
        assertThat(wasCalled).isFalse()
    }

    @Test
    fun `I do not get notified if I subscribe to an event and someone publishes a subevent of that`() = runBlockingTest {
        // arrange
        var wasCalled = false
        notifier.subscribe(SomethingHappened::class) {
            wasCalled = true
        }

        // act
        notifier.publish(SomeSubEventHappenend())

        // assert by not failing even when waiting
        assertThat(wasCalled).isFalse()
    }
}

open class SomethingHappened(override val eventId: EventId): Event {
    constructor(): this(EventId.random())
}

class SomeSubEventHappenend(eventId: EventId): SomethingHappened(eventId) {
    constructor(): this(EventId.random())
}

class SomethingElseHappened(override val eventId: EventId): Event {
    constructor(): this(EventId.random())
}