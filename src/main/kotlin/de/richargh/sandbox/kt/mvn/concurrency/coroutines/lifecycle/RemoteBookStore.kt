package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle

interface RemoteBookStore {

    suspend fun allBooks(): List<Book>
}