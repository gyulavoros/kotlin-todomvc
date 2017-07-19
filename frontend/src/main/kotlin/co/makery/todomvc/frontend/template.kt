package co.makery.todomvc.frontend

import kotlinx.html.*
import kotlinx.html.dom.create
import kotlinx.html.js.li
import org.w3c.dom.HTMLElement
import kotlin.browser.document

class Template {

  private fun defaultTemplate(todo: Todo): HTMLElement = document.create.li {
    val completed = if (todo.completed) "completed" else ""
    attributes.put("data-id", todo.id)
    classes += completed
    div(classes = "view") {
      checkBoxInput(classes = "toggle") { checked = todo.completed }
      label { +todo.title }
      button(classes = "destroy")
    }
  }

  fun show(todos: List<Todo>): String =
    todos.joinToString("\n", transform = { todo -> defaultTemplate(todo).outerHTML })

  fun itemCounter(activeCount: Int): String {
    val plural = if (activeCount == 1) "" else "s"
    return document.create.strong { +"$activeCount" }.outerHTML + " item $plural left"
  }
}
