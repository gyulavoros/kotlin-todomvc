package co.makery.todomvc.frontend

class Store {

  private val todos: MutableList<Todo> = mutableListOf()

  fun find(id: String, callback: (Todo) -> Unit) {
    todos.find { it.id == id }?.let { callback(it) }
  }

  fun find(completed: Boolean, callback: (List<Todo>) -> Unit) {
    todos.filter { it.completed == completed }.let { callback(it) }
  }

  fun findAll(callback: (List<Todo>) -> Unit) {
    callback(todos.toList())
  }

  fun save(id: String, title: String? = null, completed: Boolean? = null, callback: (List<Todo>) -> Unit) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) {
      if (title != null && completed != null) {
        todos[index] = todos[index].copy(id = id, title = title, completed = completed)
      } else if (title != null) {
        todos[index] = todos[index].copy(id = id, title = title)
      } else if (completed != null) {
        todos[index] = todos[index].copy(id = id, completed = completed)
      }
    } else if (title != null) {
      todos.add(Todo(id, title, completed ?: false))
    }
    callback(todos.toList())
  }

  fun remove(id: String, callback: () -> Unit) {
    val index = todos.indexOfFirst { it.id == id }
    if (index >= 0) todos.removeAt(index)
    callback()
  }
}
