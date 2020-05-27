package de.richargh.sandbox.kt.mvn.concurrency.datastructures

interface ChangeListener<K: Any, V> {
    fun beforePutChange(key: K, oldValue: V?, newValue: V)
    fun beforeRemove(key: K, oldValue: V?)
}