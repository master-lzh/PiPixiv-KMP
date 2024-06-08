plugins {
    id("pixiv.kmp.library.feature")
}

kotlin {
    cocoapods {
        framework {
            baseName = "home"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":common-viewmodel"))
            implementation(project(":common-ui"))
            implementation(project(":util"))
            implementation(project(":domain"))
        }
    }
}

android {
    namespace = "com.mrl.pixiv.home"
}
