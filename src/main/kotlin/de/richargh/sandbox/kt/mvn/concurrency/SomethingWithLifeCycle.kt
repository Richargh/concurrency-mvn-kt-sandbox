package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SomethingWithLifeCycle: CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    fun close(){
        job.cancel()
    }

    fun doSomething(){
        launch {

        }
    }

}