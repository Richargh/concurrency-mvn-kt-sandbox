package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Semaphore

/**
 * We "fixed" this code by making it blocking which was not my intention.
 * Without a semaphore multiple threads could by mistake overwrite map[key] with a new linked queue and we'd lose data.
 */
class SemiBlockingMultiMap<K, V>(initialCapacity: Int = 200): Iterable<Map.Entry<K, Collection<V>>> {

    private val map = ConcurrentHashMap<K, MutableCollection<V>>(initialCapacity)

    override fun iterator(): Iterator<Map.Entry<K, Collection<V>>> = map.iterator()

    private val semaphore = Semaphore(1, true)

    operator fun get(key: K?): MutableCollection<V> {
        if (key == null) return mutableListOf()

        semaphore.acquireAndRelease {
            if (!map.containsKey(key)) {
                map[key] = ConcurrentLinkedQueue()
            }
        }

        return map.getValue(key)
    }

    fun values(): Collection<V> {
        return map.values.flatten()
    }

    fun keys(): Collection<K> = map.keys
}