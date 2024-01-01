package org.toolkit4j.dispatcher

import kotlinx.coroutines.*
import java.lang.Runnable
import java.util.concurrent.atomic.AtomicBoolean

class Loop(private val codeBlock: Runnable) {

    private val stop = AtomicBoolean(false)

    private val scope = CoroutineScope(Dispatchers.Default)

    private val thread = Thread.ofVirtual().name(this.javaClass.simpleName, 0).unstarted(codeBlock)

    fun block(){
    }

    fun stop() {
        stop.set(true)
    }
}