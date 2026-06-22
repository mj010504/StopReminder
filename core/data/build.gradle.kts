plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.hilt")
}

android {
    namespace = "com.choiminjun.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(libs.play.services.location)
}
