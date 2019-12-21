package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.EventId

abstract class TestEvent: Event{
    override val eventId: EventId = EventId.random()
}