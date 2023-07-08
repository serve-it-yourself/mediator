package dev.pages.snowmerak.message

import kotlinx.serialization.Serializable

sealed class Message {
    @Serializable
    data class Subscribe(val channel: String) : Message()

    @Serializable
    data class Unsubscribe(val channel: String) : Message()

    @Serializable
    data class Send(val channel: String, val message: String) : Message()

    @Serializable
    data class Receive(val channel: String, val message: String) : Message()
}