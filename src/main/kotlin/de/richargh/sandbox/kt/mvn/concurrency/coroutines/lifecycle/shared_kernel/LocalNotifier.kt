package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

class LocalNotifier: Notifier {

    private val subscriptions = ConcurrentLinkedQueue<LocalSubscription>()

    override fun subscribe(event: KClass<out Event>, callback: (Event) -> Unit): Subscription {
        val localSubscription = LocalSubscription(event, callback)
        subscriptions.add(localSubscription)
        return localSubscription
    }

    override fun publish(event: Event){
        subscriptions.filter { it.eventType == event::class }.forEach { it.callback.invoke(event) }
    }

}