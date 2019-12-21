package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.library_published

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.BookId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.EventId

class BookReturned(
        val bookId: BookId,
        override val eventId: EventId):
        Event