package de.richargh.sandbox.kt.mvn.concurrency.coroutines.integration

import java.util.concurrent.ExecutionException

interface Call<T>{

    @Throws(InterruptedException::class, ExecutionException::class)
    fun get(): T
}