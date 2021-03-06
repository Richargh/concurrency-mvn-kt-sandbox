package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.dashboard

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.Book
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.RemoteBookStore
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Lifecycle
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Notifier
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class DashboardApp(
        private val dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore,
        private val notifier: Notifier):
        Lifecycle {

    override val coroutineContext = Job() +
                                    dispatcher +
                                    CoroutineName(javaClass.simpleName)

    private val books = mutableListOf<Book>()

    override suspend fun initialize() = coroutineScope {
        bookStore.allBooks().forEach { books.add(it) }

        Unit
    }

    override fun CoroutineScope.start() {

    }

    override fun shutdown() {
        cancel()
    }

    val bookCount
        get() = books.size
}