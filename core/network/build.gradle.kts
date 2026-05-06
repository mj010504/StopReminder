import java.util.Properties
import kotlin.apply

plugins {
    id("stopreminder.android.library")
    id("stopreminder.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.choiminjun.network"

    defaultConfig {
        val localProperties = Properties().apply {
            load(rootProject.file("local.properties").bufferedReader())
        }

        buildConfigField(
            "String",
            "TAGO_BASE_URL",
            "\"${localProperties.getProperty("TAGO_BASE_URL")}\"",
        )

        buildConfigField(
            "String",
            "TAGO_BUS_ROUTE_KEY",
            "\"${localProperties.getProperty("TAGO_BUS_ROUTE_KEY")}\"",
        )

        buildConfigField(
            "String",
            "TAGO_BUS_NODE_KEY",
            "\"${localProperties.getProperty("TAGO_BUS_NODE_KEY")}\"",
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.retrofit)
    implementation(libs.okhttp.logging.interceptor)
}
