plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "common-viewmodel"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":repository"))
            implementation(project(":domain"))
        }
    }
}

android {
    namespace = "com.mrl.pixiv.common.viewmodel"
}