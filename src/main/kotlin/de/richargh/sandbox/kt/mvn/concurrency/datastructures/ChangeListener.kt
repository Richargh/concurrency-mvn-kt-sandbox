package de.richargh.sandbox.kt.mvn.concurrency.datastructures

interface ChangeListener<K, V> {
    fun beforePutChange(key: K, newValue: V)
    fun beforeRemove(key: K, oldValue: V?)
}