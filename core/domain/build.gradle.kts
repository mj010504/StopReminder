plugins {
    id("stopreminder.kotlin.library")
    id("stopreminder.kotlin.hilt")
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.kotlinx.coroutines.core)
}
