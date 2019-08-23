package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.reflect.KClass

class LocalNotifier(private val dispatcher: CoroutineDispatcher): Notifier, CoroutineScope {

    private val job = SupervisorJob()
    override val coroutineContext
        get() = Dispatchers.Main + job

    private val subscriptions = ConcurrentLinkedQueue<LocalSubscription>()

    override fun subscribe(event: KClass<out Event>, callback: (Event) -> Unit): Subscription {
        val localSubscription = LocalSubscription(event, callback)
        subscriptions.add(localSubscription)
        return localSubscription
    }

    override fun publish(event: Event){
        subscriptions.filter { it.eventType == event::class }.forEach {
            launch(dispatcher) {
                it.callback.invoke(event)
            }
            it.callback.invoke(event)
        }
    }

}