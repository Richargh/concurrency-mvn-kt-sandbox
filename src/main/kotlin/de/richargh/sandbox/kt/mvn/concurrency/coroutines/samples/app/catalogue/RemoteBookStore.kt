package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.catalogue

interface RemoteBookStore {

    suspend fun allBooks(): List<Book>
}