plugins {
    id("pixiv.kmp.library.feature")
}

kotlin {
    cocoapods {
        framework {
            baseName = "profile"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":util"))
        }
    }
}

android {
    namespace = "com.mrl.pixiv.profile"
}
