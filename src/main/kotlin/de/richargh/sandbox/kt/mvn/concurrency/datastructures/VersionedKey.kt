package de.richargh.sandbox.kt.mvn.concurrency.datastructures

class VersionedKey<K>(val key: K, override val version: Version): Versionable