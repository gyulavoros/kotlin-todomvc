package co.makery.todomvc.frontend

import kotlin.js.Date

class Model(private val store: Store) {

  fun create(title: String, callback: (List<Todo>) -> Unit) {
    store.save(Date().getTime().toString(), title.trim(), callback = callback)
  }

  fun read(callback: (List<Todo>) -> Unit) {
    store.findAll(callback)
  }

  fun read(id: String, callback: (Todo) -> Unit) {
    store.find(id, callback)
  }

  fun read(completed: Boolean, callback: (List<Todo>) -> Unit) {
    store.find(completed, callback)
  }

  fun update(id: String, title: String? = null, completed: Boolean? = null, callback: (List<Todo>) -> Unit) {
    store.save(id, title, completed, callback)
  }

  fun remove(id: String, callback: () -> Unit) {
    store.remove(id, callback)
  }
}
