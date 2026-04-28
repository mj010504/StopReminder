plugins {
    id("stopreminder.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.choiminjun.navigation"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
}
