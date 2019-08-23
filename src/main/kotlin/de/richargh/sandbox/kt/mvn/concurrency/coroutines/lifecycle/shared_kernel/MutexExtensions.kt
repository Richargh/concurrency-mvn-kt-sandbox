package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import java.util.concurrent.Semaphore

fun Semaphore.acquireAndRelease(block: () -> Unit){
    acquire()
    block()
    release()
}