package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue_published

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.Book
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.Event
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel_api.EventId

class BookInserted(
        val book: Book,
        override val eventId: EventId):
        Event