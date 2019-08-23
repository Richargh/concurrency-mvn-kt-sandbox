package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api

import java.util.*

interface Event {
    val eventId: EventId
}

inline class EventId(private val id: String){
    companion object{
        fun random() = EventId(UUID.randomUUID().toString())
    }
}