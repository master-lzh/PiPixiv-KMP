plugins {
    id("pixiv.kmp.library.feature")
}

kotlin {
    cocoapods {
        framework {
            baseName = "login"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":common-viewmodel"))

            implementation(libs.bundles.okio)
            api(libs.compose.webview.multiplatform)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.login"
}