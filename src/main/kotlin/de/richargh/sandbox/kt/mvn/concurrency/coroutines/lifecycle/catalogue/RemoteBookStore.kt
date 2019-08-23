package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.catalogue

interface RemoteBookStore {

    suspend fun allBooks(): List<Book>
}