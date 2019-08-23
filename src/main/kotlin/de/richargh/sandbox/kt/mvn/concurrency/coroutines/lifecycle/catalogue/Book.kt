package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.Entity
import de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel.EntityId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

class Book(override val id: BookId, val title: String):
        Entity<BookId>

inline class BookId(val id: String): EntityId {
    companion object {
        fun random() = BookId(UUID.randomUUID().toString())
    }
}
