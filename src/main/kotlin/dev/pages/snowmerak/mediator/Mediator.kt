package dev.pages.snowmerak.mediator

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.internal.synchronized
import java.util.concurrent.atomic.AtomicReference

class Mediator {
    companion object {
        @OptIn(ObsoleteCoroutinesApi::class)
        private fun CoroutineScope.new() = actor<Message> {
            runBlocking {
                for (msg in channel) {
                    val sessionMap = mutableMapOf<String, Int>()
                    val list = mutableListOf<AtomicReference<DefaultWebSocketServerSession>>()

                    when (msg) {
                        is Message.Subscribe -> {
                            sessionMap[msg.id] = list.size
                            list.add(AtomicReference(msg.session))
                        }

                        is Message.Unsubscribe -> {
                            val index = sessionMap.remove(msg.id)
                            index?.let { list.removeAt(it) }
                        }

                        is Message.Broadcast -> {
                            launch {
                                list.forEach { session ->
                                    try {
                                        session.get().send(Frame.Text(msg.message))
                                    } catch (e: Exception) {
                                        println("Error: ${e.message}")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        private val map = mutableMapOf<String, SendChannel<Message>>()

        @OptIn(InternalCoroutinesApi::class)
        fun get(channel: String): SendChannel<Message> {
            return runBlocking {
                synchronized(map) {
                    map[channel]?.let { return@runBlocking it }
                    val actor = new()
                    map[channel] = actor
                    return@runBlocking actor
                }
            }
        }
    }

    sealed class Message {
        data class Subscribe(val id: String, val session: DefaultWebSocketServerSession) : Message()
        data class Unsubscribe(val id: String) : Message()
        data class Broadcast(val message: String) : Message()
    }
}
