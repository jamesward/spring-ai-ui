package com.jamesward.springaiui

import com.github.wakingrufus.htmx.HtmxEvent
import kotlinx.html.HTMLTag

/**
 * Adds an hx-on event handler to this HTML element.
 * Uses the dash format (hx-on-htmx-before-request) for XML serializer compatibility.
 *
 * @param event The HTMX event to listen for
 * @param script The JavaScript to execute when the event fires
 */
fun HTMLTag.hxOn(event: HtmxEvent, script: String) {
    // Use dash format instead of colon format for XML serializer compatibility
    // htmx:before-request -> htmx-before-request -> hx-on-htmx-before-request
    val attributeName = "hx-on-${event.htmlForm.replace(':', '-')}"
    attributes[attributeName] = script
}

/**
 * Adds an hx-on event handler for a custom (non-HTMX) event.
 * Uses the dash format for XML serializer compatibility.
 *
 * @param event The event name (e.g., "click", "submit")
 * @param script The JavaScript to execute when the event fires
 */
fun HTMLTag.hxOn(event: String, script: String) {
    attributes["hx-on-$event"] = script
}
