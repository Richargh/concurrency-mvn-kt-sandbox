package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.Entity
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel.EntityId
import java.util.*

class Book(override val id: BookId, val title: String):
        Entity<BookId>

inline class BookId(val id: String): EntityId {
    companion object {
        fun random() = BookId(UUID.randomUUID().toString())
    }
}
