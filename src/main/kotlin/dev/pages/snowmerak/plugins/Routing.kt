package dev.pages.snowmerak.plugins

import dev.pages.snowmerak.message.Message
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.configureRouting() {
    routing {
        get("/") {
            val obj = Message.Receive(channel = "index.index", message = "Hello, World!")
            val data = Json.encodeToString(obj)
            call.respondText(data)
        }
    }
}
