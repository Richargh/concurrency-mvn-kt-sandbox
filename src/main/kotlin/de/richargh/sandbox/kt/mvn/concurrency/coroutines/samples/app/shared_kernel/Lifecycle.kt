package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import kotlinx.coroutines.CoroutineScope

interface Lifecycle: CoroutineScope {

    suspend fun initialize()

    fun CoroutineScope.start()

    fun shutdown()
}