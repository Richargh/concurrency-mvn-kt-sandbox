package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import kotlin.reflect.KClass

class LocalSubscription(val eventType: KClass<out Event>, val callback: (Event) -> Unit): Subscription {

    override fun close() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}