plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.choiminjun.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(libs.kotlinx.serialization.json)
}
