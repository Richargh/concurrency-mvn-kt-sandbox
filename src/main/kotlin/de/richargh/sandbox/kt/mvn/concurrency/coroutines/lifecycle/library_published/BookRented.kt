package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.library_published

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.BookId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel_api.EventId

class BookRented(
        val bookId: BookId,
        override val eventId: EventId):
        Event