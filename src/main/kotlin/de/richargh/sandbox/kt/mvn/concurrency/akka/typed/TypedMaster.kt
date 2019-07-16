package de.richargh.sandbox.kt.mvn.concurrency.akka.typed

import de.richargh.sandbox.kt.mvn.concurrency.akka.routing.Work

interface TypedMaster {
    fun dispatchWork(work: Work)
}