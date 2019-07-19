package de.richargh.sandbox.kt.mvn.concurrency

import kotlinx.coroutines.*
import java.util.concurrent.ForkJoinPool
import kotlin.coroutines.AbstractCoroutineContextElement
import kotlin.coroutines.CoroutineContext

class NewContextElement: AbstractCoroutineContextElement(Key) {
    companion object Key: CoroutineContext.Key<NewContextElement>

    private var content = "None"

    fun printContent() {
        println("My content is: $content")
    }

    fun changeContent() {
        content = "Some"
    }
}

//class VideoStoreWithCustomContext(private val parentContext: CoroutineContext): CoroutineContext {
//
//    private val contextElements = hashMapOf<>()
//
//    override fun <R> fold(initial: R, operation: (R, CoroutineContext.Element) -> R): R {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun <E: CoroutineContext.Element> get(key: CoroutineContext.Key<E>): E? {
//
//    }
//
//    override fun minusKey(key: CoroutineContext.Key<*>): CoroutineContext {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    suspend fun doThisFirst() = coroutineScope {
//
//    }
//
//    private fun childDoesSomething(){
//
//    }
//}
