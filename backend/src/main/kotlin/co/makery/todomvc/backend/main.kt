package co.makery.todomvc.backend

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.DefaultHeaders
import io.ktor.html.respondHtml
import io.ktor.routing.Routing
import io.ktor.routing.get
import kotlinx.html.*

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
          script(src = "frontend/frontend.bundle.js") {}
        }
      }
    }
  }
}
