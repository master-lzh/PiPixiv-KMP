plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "util"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(kotlinx.datetime)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.util"
}