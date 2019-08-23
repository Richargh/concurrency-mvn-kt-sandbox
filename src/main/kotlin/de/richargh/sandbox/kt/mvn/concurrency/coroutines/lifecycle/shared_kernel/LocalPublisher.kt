package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event

class LocalPublisher: Publisher {
    override fun <AnyEvent: Event> subscribe(notification: (AnyEvent) -> Unit): Subscription<AnyEvent> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun publish(event: Event) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}