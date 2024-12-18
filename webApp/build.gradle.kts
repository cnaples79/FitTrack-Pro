plugins {
    kotlin("js")
    kotlin("plugin.serialization")
}

kotlin {
    js(IR) {
        browser {
            commonWebpackConfig {
                cssSupport {
                    enabled.set(true)
                }
            }
            binaries.executable()
        }
    }
}

dependencies {
    implementation(project(":shared"))
    
    // React
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
    
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
    
    // Material UI
    implementation("org.jetbrains.kotlin-wrappers:kotlin-mui:5.8.7-pre.346")
    implementation("org.jetbrains.kotlin-wrappers:kotlin-mui-icons:5.8.7-pre.346")
    
    // Date/Time
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
    
    // Charts
    implementation(npm("react-chartjs-2", "4.3.1"))
    implementation(npm("chart.js", "3.9.1"))
}
