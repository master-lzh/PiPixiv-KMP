plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "network"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(kotlinx.coroutines.android)
            implementation(kotlinx.ktor.client.okhttp)
        }
        commonMain.dependencies {
            implementation(project(":repository"))
            implementation(project(":domain"))
            // Ktor
            implementation(kotlinx.ktor.client.core)
            implementation(kotlinx.ktor.client.logging)
            implementation(kotlinx.ktor.client.content.negotiation)
            implementation(kotlinx.ktor.serialization.kotlinx.json)

            implementation(kotlinx.datetime)
        }
        iosMain.dependencies {
            implementation(kotlinx.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.network"
}