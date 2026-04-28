plugins {
    id("stopreminder.kotlin.library")
    id("stopreminder.kotlin.hilt")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
