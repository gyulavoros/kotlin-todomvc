package co.makery.todomvc.frontend

class Controller(private val model: Model, private val view: View) {

  private var activeRoute = "All"
  private var lastActiveRoute = ""

  init {
    view.bind.newTodo { addItem(it) }
    view.bind.itemEdit { editItem(it) }
    view.bind.itemEditDone { id, title -> editItemSave(id, title) }
    view.bind.itemEditCancel { editItemCancel(it) }
    view.bind.itemRemove { removeItem(it) }
    view.bind.itemToggle { id, completed -> toggleComplete(id, completed) }
    view.bind.removeCompleted { removeCompletedItems() }
    view.bind.toggleAll { toggleAll(it) }
  }

  fun setView(locationHash: String) {
    val page = locationHash.split("/").elementAtOrElse(1, { _ -> "" })
    updateFilterState(page)
  }

  private fun showAll() {
    model.read { view.render.showEntries(it) }
  }

  private fun showActive() {
    model.read(false) { view.render.showEntries(it) }
  }

  private fun showCompleted() {
    model.read(true) { view.render.showEntries(it) }
  }

  private fun addItem(title: String) {
    if (title.isBlank()) return
    model.create(title) {
      view.render.clearNewTodo()
      filter(true)
    }
  }

  private fun editItem(id: String) {
    model.read(id) { todo -> view.render.editItem(id, todo.title) }
  }

  private fun editItemSave(id: String, _title: String) {
    val title = _title.trim()
    if (title.isNotBlank()) {
      model.update(id, title) {
        view.render.editItemDone(id, title)
      }
    } else {
      removeItem(id)
    }
  }

  private fun editItemCancel(id: String) {
    model.read(id) { (_, title) ->
      view.render.editItemDone(id, title)
    }
  }

  private fun removeItem(id: String) {
    model.remove(id) {
      view.render.removeItem(id)
    }
    filter()
  }

  private fun removeCompletedItems() {
    model.read(true) { todos ->
      todos.map { it.id }.forEach { removeItem(it) }
    }
    filter()
  }

  private fun toggleComplete(id: String, completed: Boolean, silent: Boolean = false) {
    model.update(id, completed = completed) {
      view.render.elementComplete(id, completed)
    }
    if (silent.not()) filter()
  }

  private fun toggleAll(completed: Boolean) {
    model.read(completed.not()) { todos ->
      todos.forEach { (id, _, _) -> toggleComplete(id, completed, true) }
    }
    filter()
  }

  private fun updateCount() {
    model.read { todos ->
      view.render.updateElementCount(todos.active())
      view.render.clearCompletedButton(todos.completed(), todos.isNotEmpty())
      view.render.toggleAll(todos.completed() == todos.total())
      view.render.contentBlockVisibility(todos.isNotEmpty())
    }
  }

  private fun filter(force: Boolean = false) {
    val activeRoute = activeRoute[0].toUpperCase() + activeRoute.substring(1)
    updateCount()
    if (force || lastActiveRoute != "All" || lastActiveRoute != activeRoute) {
      when (activeRoute) {
        "All" -> showAll()
        "Active" -> showActive()
        "Completed" -> showCompleted()
      }
    }
    lastActiveRoute = activeRoute
  }

  private fun updateFilterState(currentPage: String) {
    activeRoute = currentPage
    if (currentPage.isBlank()) {
      activeRoute = "All"
    }
    filter()
    view.render.setFilter(currentPage)
  }
}
