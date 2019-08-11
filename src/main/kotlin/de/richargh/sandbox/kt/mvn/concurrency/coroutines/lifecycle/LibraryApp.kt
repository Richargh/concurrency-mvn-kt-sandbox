package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

class LibraryApp(
        private val dispatcher: CoroutineDispatcher,
        private val bookStore: RemoteBookStore): Lifecycle {

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