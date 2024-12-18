plugins {
    kotlin("jvm") version "1.9.20" apply false
    kotlin("multiplatform") version "1.9.20" apply false
    kotlin("android") version "1.9.20" apply false
    kotlin("js") version "1.9.20" apply false
    id("com.android.application") version "8.1.1" apply false
    id("com.android.library") version "8.1.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20" apply false
    id("com.squareup.sqldelight") version "1.5.5" apply false
}

subprojects {
    tasks.withType<Delete> {
        delete(project.buildDir)
    }
}
