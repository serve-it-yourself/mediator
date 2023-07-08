package dev.pages.snowmerak.plugins

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/json") {
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    val text = frame.readText()
                }
            }
        }
//        webSocket("/proto") {
//            for (frame in incoming) {
//                if (frame is Frame.Text) {
//                    val text = frame.readText()
//                }
//            }
//        }
    }
}
