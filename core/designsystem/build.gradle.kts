plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.compose")
}

android {
    namespace = "com.choiminjun.designsystem"
}

dependencies {
    implementation(libs.androidx.compose.material.icons)
}
