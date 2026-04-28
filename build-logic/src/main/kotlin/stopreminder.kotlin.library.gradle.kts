import com.choiminjun.build.logic.configureKotlin
import com.choiminjun.build.logic.configureTest
import org.gradle.kotlin.dsl.kotlin

plugins {
    kotlin("jvm")
}

configureKotlin()
configureTest()
