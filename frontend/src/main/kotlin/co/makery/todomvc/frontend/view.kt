package co.makery.todomvc.frontend

import org.w3c.dom.*
import org.w3c.dom.events.KeyboardEvent
import kotlin.browser.document
import kotlin.dom.addClass
import kotlin.dom.removeClass

private const val ENTER_KEY = 13
private const val ESCAPE_KEY = 27

class View(private val template: Template) {

  val render = Renderer()
  val bind = Binder()

  private val todoList = qs(".todo-list") as HTMLElement
  private val todoItemCounter = qs(".todo-count") as HTMLElement
  private val clearCompleted = qs(".clear-completed") as HTMLElement
  private val main = qs(".main") as HTMLElement
  private val footer = qs(".footer") as HTMLElement
  private val toggleAll = qs(".toggle-all") as HTMLInputElement
  private val newTodo = qs(".new-todo") as HTMLInputElement

  inner class Renderer {
    fun showEntries(todos: List<Todo>) {
      todoList.innerHTML = template.show(todos)
    }

    fun removeItem(id: String) = item(id)?.let { todoList.removeChild(it) }

    fun updateElementCount(activeCount: Int) {
      todoItemCounter.innerHTML = template.itemCounter(activeCount)
    }

    fun clearCompletedButton(completedCount: Int, visible: Boolean) {
      clearCompleted.innerHTML = if (completedCount > 0) "Clear completed" else ""
      clearCompleted.style.display = if (visible) "block" else "none"
    }

    fun contentBlockVisibility(visible: Boolean) {
      val display = if (visible) "block" else "none"
      main.style.display = display
      footer.style.display = display
    }

    fun toggleAll(checked: Boolean) {
      toggleAll.checked = checked
    }

    fun setFilter(currentPage: String) {
      qs(".filters .selected")?.className = ""
      qs(".filters [href=\"#/$currentPage\"")?.className = "selected"
    }

    fun clearNewTodo() {
      newTodo.value = ""
    }

    fun elementComplete(id: String, completed: Boolean) {
      item(id)?.let { listItem ->
        listItem.className = if (completed) "completed" else ""
        (qs("input", listItem) as HTMLInputElement).checked = completed
      }
    }

    fun editItem(id: String, title: String) {
      item(id)?.let { listItem ->
        listItem.addClass("editing")
        val input = document.createElement("input") as HTMLInputElement
        input.addClass("edit")
        listItem.appendChild(input)
        input.focus()
        input.value = title
      }
    }

    fun editItemDone(id: String, title: String) {
      item(id)?.let { listItem ->
        val input = qs("input.edit", listItem) as HTMLInputElement
        listItem.removeChild(input)
        listItem.removeClass("editing")
        qsa("label", listItem).forEach { label ->
          label.textContent = title
        }
      }
    }
  }

  inner class Binder {
    fun newTodo(handler: (String) -> Unit) {
      newTodo.addEventListener("change", { _ ->
        handler(newTodo.value)
      })
    }

    fun removeCompleted(handler: () -> Unit) {
      clearCompleted.addEventListener("click", { _ -> handler() })
    }

    fun toggleAll(handler: (Boolean) -> Unit) {
      toggleAll.addEventListener("click", { _ -> handler(toggleAll.checked) })
    }

    fun itemEdit(handler: (String) -> Unit) {
      delegate(todoList, "li label", "dblclick", { et, _ ->
        handler(itemId(et as Element))
      })
    }

    fun itemRemove(handler: (String) -> Unit) {
      delegate(todoList, ".destroy", "click", { et, _ ->
        handler(itemId(et as Element))
      })
    }

    fun itemToggle(handler: (String, Boolean) -> Unit) {
      delegate(todoList, ".toggle", "click", { et, _ ->
        val target = et as HTMLInputElement
        handler(itemId(target), target.checked)
      })
    }

    fun itemEditDone(handler: (String, String) -> Unit) {
      delegate(todoList, "li .edit", "blur", { et, _ ->
        val target = et as HTMLInputElement
        if (target.dataset["iscanceled"] != "true") {
          handler(itemId(target), target.value)
        }
      })
      delegate(todoList, "li .edit", "keypress", { et, e ->
        if (e is KeyboardEvent && e.keyCode == ENTER_KEY) {
          val target = et as HTMLInputElement
          target.blur()
        }
      })
    }

    fun itemEditCancel(handler: (String) -> Unit) {
      delegate(todoList, "li .edit", "keyup", { et, e ->
        if (e is KeyboardEvent && e.keyCode == ESCAPE_KEY) {
          val target = et as HTMLInputElement
          target.dataset["iscanceled"] = "true"
          target.blur()
          handler(itemId(target))
        }
      })
    }
  }

  private fun itemId(element: Element): String {
    val li = parent(element, "li") as? HTMLElement
    return li?.dataset?.get("id") ?: ""
  }

  private fun item(id: String): Element? = qs("[data-id=\"$id\"]")
}
