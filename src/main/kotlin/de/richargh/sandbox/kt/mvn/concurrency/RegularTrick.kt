package de.richargh.sandbox.kt.mvn.concurrency

class RegularTrick(private val publisher: Publisher){

    fun doRegular(){
        publisher.publish("doRegular")
    }

}