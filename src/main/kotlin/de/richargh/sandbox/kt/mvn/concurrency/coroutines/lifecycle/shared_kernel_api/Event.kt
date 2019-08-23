package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api

interface Event {
    val eventId: EventId
}

inline class EventId(private val id: String)