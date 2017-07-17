package co.makery.todomvc.backend

import kotlinx.html.*
import org.jetbrains.ktor.application.Application
import org.jetbrains.ktor.application.call
import org.jetbrains.ktor.application.install
import org.jetbrains.ktor.features.DefaultHeaders
import org.jetbrains.ktor.html.respondHtml
import org.jetbrains.ktor.logging.CallLogging
import org.jetbrains.ktor.routing.Routing
import org.jetbrains.ktor.routing.get

fun Application.main() {
  install(DefaultHeaders)
  install(CallLogging)
  install(Routing) {
    get("/") {
      call.respondHtml {
        head {
          title { +"HTML Application" }
          style { +CSS.index() }
        }
        body {
          script(src = "frontend/frontend.bundle.js")
        }
      }
    }
  }
}

fun FlowContent.widget(body: FlowContent.() -> Unit) {
  div { body() }
}
