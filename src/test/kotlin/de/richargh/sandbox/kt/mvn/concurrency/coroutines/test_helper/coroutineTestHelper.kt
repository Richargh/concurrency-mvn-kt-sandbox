package de.richargh.sandbox.kt.mvn.concurrency.coroutines.test_helper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.fail
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

fun testBlocking(context: CoroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher(),
                 block: suspend CoroutineScope.() -> Unit): Unit = runBlocking(context) {
    if (launch { block() }.isCancelled) fail("Probably an exception was swallowed.")
}