plugins {
    id("org.jetbrains.kotlin.multiplatform")
}

group = "com.mrl.pixiv"
version = "1.0-SNAPSHOT"

kotlin {
    jvm()
    sourceSets {
        jvmMain {
            dependencies {
                implementation("com.squareup:javapoet:1.13.0")
                implementation(kotlinx.ksp.symbol.processing.api)
            }
            kotlin.srcDir("src/main/kotlin")
            resources.srcDir("src/main/resources")
        }
    }
}
