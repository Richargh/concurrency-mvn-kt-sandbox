package de.richargh.sandbox.kt.mvn.concurrency

class Fibo(private val publisher: Publisher){

    suspend fun doSth(){
        publisher.publish("doSth")
    }

}