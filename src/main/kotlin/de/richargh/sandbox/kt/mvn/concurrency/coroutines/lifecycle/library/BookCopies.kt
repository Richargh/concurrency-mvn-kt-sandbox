package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.library

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue.BookId
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Entity

class BookCopies(override val id: BookId, val title: String, val count: Int):
        Entity<BookId>