package de.richargh.sandbox.kt.mvn.concurrency.coroutines.intergration

import de.richargh.sandbox.kt.mvn.concurrency.coroutines.integration.Call

class FakeCall(private val data: String?): Call<String> {
    override fun get() = data
                         ?: throw InterruptedException("I wasn't interrupted but I have no data")
}