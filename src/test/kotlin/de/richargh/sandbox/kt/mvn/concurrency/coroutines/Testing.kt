package de.richargh.sandbox.kt.mvn.concurrency.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun runBlockingTest(
        context: CoroutineContext = EmptyCoroutineContext,
        block: suspend CoroutineScope.() -> Any) {
    runBlocking(context, block)
}