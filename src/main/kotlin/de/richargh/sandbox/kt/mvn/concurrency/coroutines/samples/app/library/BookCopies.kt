package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.library

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue.BookId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Entity

class BookCopies(override val id: BookId, val title: String, val count: Int):
        Entity<BookId>