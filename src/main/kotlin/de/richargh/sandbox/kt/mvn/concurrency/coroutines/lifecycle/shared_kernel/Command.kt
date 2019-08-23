package de.richargh.sandbox.kt.mvn.concurrency.coroutines.lifecycle.shared_kernel

interface Command {
    val id: CommandId
}

inline class CommandId(private val id: String)