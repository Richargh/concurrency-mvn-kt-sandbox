package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import kotlin.reflect.KClass

interface Index<PrimaryK: Any, V>: ChangeListener<PrimaryK, V> {
    fun responsibleForKey(any: KClass<out Any>): Boolean
    fun resolveKey(otherK: Any): PrimaryK?
    fun resolveKeys(otherK: Any): List<PrimaryK>?
}