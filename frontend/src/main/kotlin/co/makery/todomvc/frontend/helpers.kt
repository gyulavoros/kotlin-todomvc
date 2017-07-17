package co.makery.todomvc.frontend

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.ParentNode
import org.w3c.dom.asList
import org.w3c.dom.events.Event
import org.w3c.dom.events.EventTarget
import kotlin.browser.document

fun qs(selector: String, scope: Element? = null) = node(scope).querySelector(selector) as HTMLElement?

fun qsa(selector: String, scope: Element? = null) = node(scope).querySelectorAll(selector).asList().map { it as HTMLElement }

fun delegate(target: Element, selector: String, type: String, handler: (EventTarget, Event) -> Unit) {
  val dispatchEvent: (event: Event) -> Unit = { event ->
    event.target?.let {
      val targetElement = it
      val potentialElements = qsa(selector, target)
      val hasMatch = potentialElements.contains(targetElement)
      if (hasMatch) {
        handler(targetElement, event)
      }
    }
  }
  val useCapture = type == "blur" || type == "focus"
  target.addEventListener(type, dispatchEvent, !!useCapture)
}

fun parent(element: Element, tagName: String): Element? {
  val parentNode = element.parentNode as Element? ?: return null
  if (parentNode.tagName.toLowerCase() == tagName.toLowerCase()) return parentNode
  return parent(parentNode, tagName)
}

private fun node(scope: Element?): ParentNode = scope ?: document
