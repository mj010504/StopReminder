plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "stopreminder.android.hilt"
            implementationClass = "com.choiminjun.build.logic.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "stopreminder.kotlin.hilt"
            implementationClass = "com.choiminjun.build.logic.HiltKotlinPlugin"
        }
    }
}
