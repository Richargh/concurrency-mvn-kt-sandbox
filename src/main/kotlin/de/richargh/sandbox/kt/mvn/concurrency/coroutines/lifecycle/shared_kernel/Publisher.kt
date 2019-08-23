package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event

interface Publisher {
    fun <AnyEvent> subscribe(notification: (AnyEvent) -> Unit): Subscription<AnyEvent> where AnyEvent: Event

    fun publish(event: Event)
}