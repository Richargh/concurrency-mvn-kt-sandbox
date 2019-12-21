package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import kotlin.reflect.KClass

interface Notifier {

    fun subscribe(event: KClass<out Event>, callback: (Event) -> Unit): Subscription

    fun publish(event: Event)
}