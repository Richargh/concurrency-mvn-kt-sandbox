package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.EventId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class EventSpyTest{

    @Test
    fun `foo`() {
        val someEvent = SomeEvent(payload = "unimportant")

        // arrange
        val notifier = LocalNotifierBuilder().build()
        val spy = EventSpy.spy(notifier, SomeEvent::class)

        // act
        notifier.publish(someEvent)

        // assert
        assertThat(spy.events).containsExactly(someEvent)
    }
}

private data class SomeEvent(
        val payload: String,
        override val eventId: EventId = EventId.random()): Event