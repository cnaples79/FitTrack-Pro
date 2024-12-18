package com.fittrackpro.web

import com.fittrackpro.shared.di.initKoin
import com.fittrackpro.shared.data.DatabaseDriverFactory
import kotlinx.browser.document
import react.create
import react.dom.client.createRoot

fun main() {
    // Initialize Koin
    initKoin(DatabaseDriverFactory()) {}
    
    // Create root element
    val container = document.getElementById("root") ?: error("Couldn't find root container!")
    createRoot(container).render(App.create())
}
