plugins {
    id("stopreminder.android.library")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.choiminjun.navigation"
}

dependencies {
    implementation(libs.androidx.navigation.ui)
    implementation(libs.kotlinx.serialization.json)
}
