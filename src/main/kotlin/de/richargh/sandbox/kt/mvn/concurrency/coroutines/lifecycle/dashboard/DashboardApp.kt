package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.dashboard

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.Book
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.RemoteBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Notifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

class DashboardApp(
        private val dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + dispatcher + CoroutineName("LibraryApp")

    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
        bookStore.allBooks().forEach { books.add(it) }

        Unit
    }

    override fun start() {
    }

    override fun shutdown() {
        job.cancel()
    }

    val bookCount
        get() = books.size
}