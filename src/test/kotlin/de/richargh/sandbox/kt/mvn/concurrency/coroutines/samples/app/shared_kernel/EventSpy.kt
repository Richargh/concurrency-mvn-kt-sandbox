package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

class EventSpy{

    val events = ConcurrentLinkedQueue<Event>()

    private fun onEvent(event: Event){
        events.add(event)
    }

    companion object {
        fun spy(notifier: Notifier, firstEvent: KClass<out Event>, vararg otherEvents: KClass<out Event>): EventSpy{
            val eventSpy = EventSpy()

            notifier.subscribe(firstEvent, eventSpy::onEvent)
            otherEvents.forEach {
                notifier.subscribe(it, eventSpy::onEvent)
            }

            return eventSpy
        }
    }
}