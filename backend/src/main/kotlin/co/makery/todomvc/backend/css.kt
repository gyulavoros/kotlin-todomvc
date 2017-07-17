package co.makery.todomvc.backend

import azadev.kotlin.css.*
import azadev.kotlin.css.dimens.percent

object CSS {

  fun index(): String = Stylesheet {
    html and body {
      margin = 0
      padding = 0
    }
    button {
      margin = 0
      padding = 0
      border = 0
      background = "none"
      fontSize = 100.percent
      verticalAlign = "baseline"
      fontFamily = "inherit"
      fontWeight = "inherit"
      color = "inherit"
    }
    h1 {
      color = 0xFF0000
    }
  }.render()
}
