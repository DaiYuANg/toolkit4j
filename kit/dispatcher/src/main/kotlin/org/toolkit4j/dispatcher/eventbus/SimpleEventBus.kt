package org.toolkit4j.dispatcher.eventbus

class Subscriber {}

data class Event(
    private val a: Long = System.currentTimeMillis(),
)

class SimpleEventBus
@JvmOverloads constructor(
    private val subscribers: MutableSet<Subscriber> = mutableSetOf(),
) : MutableSet<Subscriber> by subscribers {


    fun publish(event: Event) {
        subscribers.forEach { }
    }

    fun publish(events: Collection<Event>) {

    }
}