package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue_published

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.Book
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.EventId

class BookInserted(
        val book: Book,
        override val eventId: EventId):
        Event