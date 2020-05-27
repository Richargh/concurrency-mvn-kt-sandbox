package de.richargh.sandbox.kt.mvn.concurrency.datastructures

import java.util.concurrent.ConcurrentHashMap

fun <K: Any, V: Versionable> ConcurrentHashMap<K, V>.putIfNewer(key: K, value: V, beforePut: (V) -> Unit) =
        compute(key) { _, existing: V? ->
            when {
                existing == null || existing.version < value.version -> {
                    beforePut(value)
                    value
                }
                else                                                 -> {
                    existing
                }
            }
        }

fun <K: Any, V: Versionable> ConcurrentHashMap<K, V>.removeIfNewer(key: K, maxVersionToDelete: Version, beforeRemove: (V?) -> Unit) =
        compute(key) { _, existing: V? ->
            // returning null removes the entry
            when {
                existing == null || existing.version < maxVersionToDelete -> {
                    beforeRemove(existing)
                    null
                }
                else                                                      -> {
                    existing
                }
            }
        }