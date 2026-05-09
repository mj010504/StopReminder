plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.hilt")
}

android {
    namespace = "com.choiminjun.database"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}
