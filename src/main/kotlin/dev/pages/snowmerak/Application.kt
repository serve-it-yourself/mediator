package dev.pages.snowmerak

import io.ktor.server.application.*
import dev.pages.snowmerak.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureSecurity()
    configureHTTP()
    configureSerialization()
    configureSockets()
    configureRouting()
}
