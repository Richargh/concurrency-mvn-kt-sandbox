package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.EventId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper.testBlocking
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

abstract class NotifierContract(private val notifier: Notifier) {

    @Test
    fun `I don't get notified if I subscribe to an event and no one publishes an event`() = testBlocking {
        // arrange

        // act
        notifier.subscribe(SomethingHappened::class) {
            fail("I should never be called because no one published")
        }

        // assert by not failing even when waiting
        delay(200)
    }

    @Test
    fun `I get notified if I subscribe to an event and someone publishes that`() = testBlocking {
        // arrange
        var callCount = 0
        notifier.subscribe(SomethingHappened::class) {
            callCount++
        }

        // act
        notifier.publish(SomethingHappened())
        delay(100)

        // assert by not failing even when waiting
        assertThat(callCount).isEqualTo(1)
    }

    @Test
    fun `I do not get notified if I subscribe to an event and someone publishes a different event`() = testBlocking {
        // arrange
        var callCount = 0
        notifier.subscribe(
                SomethingHappened::class) {
            callCount++
        }

        // act
        coroutineScope {
            notifier.publish(SomethingElseHappened())
        }

        // assert by not failing even when waiting
        assertThat(callCount).isEqualTo(0)
    }

    @Test
    fun `I do not get notified if I subscribe to an event and someone publishes a subevent of that`() = testBlocking {
        // arrange
        var callCount = 0
        notifier.subscribe(SomethingHappened::class) {
            callCount++
        }

        // act
        coroutineScope {
            notifier.publish(SomeSubEventHappenend())
        }

        // assert by not failing even when waiting
        assertThat(callCount).isEqualTo(0)
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