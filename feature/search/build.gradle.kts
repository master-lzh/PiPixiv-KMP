plugins {
    id("pixiv.kmp.library.feature")
}

kotlin {
    cocoapods {
        framework {
            baseName = "search"
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
    namespace = "com.mrl.pixiv.search"
}