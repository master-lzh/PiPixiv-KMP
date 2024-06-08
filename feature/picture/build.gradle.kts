plugins {
    id("pixiv.kmp.library.feature")
}

kotlin {
    cocoapods {
        framework {
            baseName = "picture"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":util"))
            implementation(project(":network"))

            implementation(libs.bundles.calf)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.picture"
}
