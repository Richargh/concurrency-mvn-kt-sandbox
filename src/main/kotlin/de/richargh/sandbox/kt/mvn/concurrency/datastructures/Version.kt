package de.richargh.sandbox.kt.mvn.concurrency.datastructures

data class Version(val rawValue: Long){
    operator fun compareTo(other: Version) = this.rawValue.compareTo(other.rawValue)
}