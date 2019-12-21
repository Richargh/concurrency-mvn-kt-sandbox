package de.richargh.sandbox.kt.mvn.concurrency.coroutines.samples.app.shared_kernel

import kotlinx.coroutines.Dispatchers

class LocalNotifierTest: NotifierContract(
        LocalNotifier(Dispatchers.Default))