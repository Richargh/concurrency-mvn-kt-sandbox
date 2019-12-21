package de.richargh.sandbox.kt.mvn.concurrency.coroutines.integration

import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * Example of await for a Future/Promise style library that does not support Coroutines and for which there is no standard extension function.
 *
@see <a href="https://youtu.be/9HUFo4WyDPI?t=1225">Kotlin Coroutines Presentation and Q&A by Roman Elizarov</a>
 */
suspend fun <T> Call<T>.await(): T = suspendCoroutine { continuation: Continuation<T> ->
    /** integrate with other code here */

    // NOTE that you should use the mechanisms from the code you want to integrate, see the video above for more details
    // here we are free to do it this way, because we wrote the Call<> interface
    try {
        val result: T = this.get()
        continuation.resume(result)
    } catch (t: Throwable) {
        continuation.resumeWithException(t)
    }
}