plugins {
    // Keep apply(false) for plugins you do NOT want to apply at the root
    id("com.android.application").version("8.1.2").apply(false)
    id("com.android.library").version("8.1.2").apply(false)
    kotlin("android").version("1.9.20").apply(false)

    // Remove apply(false) here so that the kotlin multiplatform plugin is actually applied at the root
    kotlin("multiplatform").version("1.9.20")

    // Keep apply(false) if you don't need them at the root
    kotlin("plugin.serialization").version("1.9.20").apply(false)
    id("app.cash.sqldelight").version("2.0.0").apply(false)
}

import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin

// Configure Node.js download after NodeJsRootPlugin is available
rootProject.plugins.withType<NodeJsRootPlugin> {
    rootProject.extensions.getByType(NodeJsRootExtension::class.java).apply {
        download = true
        nodeVersion = "18.12.1" // Use your desired Node.js version
    }
}
