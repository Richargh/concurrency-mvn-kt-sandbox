package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.library

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.Book
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.RemoteBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Notifier
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LibraryApp(
        private val dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    private val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = job + dispatcher + CoroutineName("LibraryApp")

    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
    }

    override fun start() {
    }

    override fun shutdown() {
        job.cancel()
    }
}