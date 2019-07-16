package de.richargh.sandbox.kt.mvn.concurrency.akka.typed

import akka.actor.TypedActor;
import akka.actor.*;
import akka.japi.*;
import akka.dispatch.Futures;

import scala.concurrent.Await;
import scala.concurrent.Future;

interface Squarer{
    fun squareDontCare(i: Int)  // fire-forget

    fun square(i: Int): Future<Int>  // non-blocking send-request-reply

    fun squareNowPlease(i: Int): Option<Int>  // blocking send-request-reply

    fun squareNow(i: Int): Int  // blocking send-request-reply
}