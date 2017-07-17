package co.makery.todomvc.frontend

data class Todo(val id: String, val title: String, val completed: Boolean = false)

fun List<Todo>.active(): Int = count { it.completed.not() }

fun List<Todo>.completed(): Int = count { it.completed }

fun List<Todo>.total(): Int = size
