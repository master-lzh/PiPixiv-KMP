plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "common-ui"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":util"))
            implementation(project(":common-viewmodel"))
            implementation(project(":domain"))

            implementation(kotlinx.bundles.serialization)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.common_ui"
}