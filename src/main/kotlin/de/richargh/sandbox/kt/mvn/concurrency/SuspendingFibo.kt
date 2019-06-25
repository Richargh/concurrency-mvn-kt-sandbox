package de.richargh.sandbox.kt.mvn.concurrency

class SuspendingFibo(private val publisher: Publisher){

    private val regularTrick = RegularTrick(publisher)

    suspend fun doSth(){
        publisher.publish("doSth")
        regularTrick.doRegular()
    }

}