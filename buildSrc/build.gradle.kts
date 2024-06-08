plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(kotlinx.kotlin.gradlePlugin)
    // https://mvnrepository.com/artifact/org.jetbrains.compose/compose-gradle-plugin
    implementation(libs.compose.gradle.plugin)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin.native.cocoapods/org.jetbrains.kotlin.native.cocoapods.gradle.plugin
    implementation(kotlinx.kotlin.native.cocoapods.gradlepPlugin)
}

gradlePlugin {
    plugins {
        create("kmpApplication") {
            id = "pixiv.kmp.application"
            implementationClass = "KmpApplicationConventionPlugin"
        }
        create("kmpLibrary") {
            id = "pixiv.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        create("kmpFeature") {
            id = "pixiv.kmp.library.feature"
            implementationClass = "KmpFeatureConventionPlugin"
        }
    }
}