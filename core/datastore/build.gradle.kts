plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.hilt")
}

android {
    namespace = "com.choiminjun.datastore"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(libs.androidx.datastore.preferences)
}
