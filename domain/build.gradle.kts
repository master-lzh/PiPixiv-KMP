plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "domain"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":repository"))
            implementation(project(":util"))
        }
    }
}

android {
    namespace = "com.mrl.pixiv.domain"
}