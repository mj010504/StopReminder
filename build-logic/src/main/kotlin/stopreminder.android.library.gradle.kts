import com.choiminjun.build.logic.configureCoroutineAndroid
import com.choiminjun.build.logic.configureHiltAndroid
import com.choiminjun.build.logic.configureKotlinAndroid


plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()
configureCoroutineAndroid()
