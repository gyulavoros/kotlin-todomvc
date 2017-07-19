package co.makery.todomvc.frontend

import kotlinx.html.*
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.browser.window

external fun require(module: String): dynamic

class TodoApp(name: String) {

  init {
    val store = Store()
    val model = Model(store)
    val template = Template()
    val view = View(template)
    val controller = Controller(model, view)

    val setView: (Event) -> Unit = { _ -> document.location?.hash?.let { controller.setView(it) } }
    window.addEventListener("load", setView)
    window.addEventListener("hashchange", setView)

    println("$name started.")
  }
}

fun main(args: Array<String>) {
  require("./base.css")
  require("./index.css")

  document.addEventListener("DOMContentLoaded", {
    val body = document.body as HTMLElement
    body.append { application() }
    TodoApp("todos-kotlin")
  })
}

fun TagConsumer<*>.application() {
  section(classes = "todoapp") {
    header(classes = "header") {
      h1 { +"todos" }
      input(classes = "new-todo") { placeholder = "What needs to be done?"; autoFocus = true }
    }
    section(classes = "main") {
      input(classes = "toggle-all", type = InputType.checkBox)
      label { for_ = "toggle-all"; +"Mark all as complete" }
      ul(classes = "todo-list")
    }
    footer(classes = "footer") {
      span(classes = "todo-count")
      ul(classes = "filters") {
        li { a(classes = "selected", href = "#/") { +"All" } }
        li { a(href = "#/active") { +"Active" } }
        li { a(href = "#/completed") { +"Completed" } }
      }
      button(classes = "clear-completed") { +"Clear completed" }
    }
  }
  footer(classes = "info") {
    p { +"Double-click to edit a todo" }
    p { +"Created by "; a(href = "https://github.com/gyulavoros/kotlin-todomvc") { +"Gyula Voros" } }
    p { +"Part of "; a(href = "http://todomvc.com") { +"TodoMVC" } }
  }
}
