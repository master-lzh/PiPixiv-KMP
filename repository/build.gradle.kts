plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "repository"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":datasource"))
            implementation(project(":data"))
            implementation(project(":util"))
        }
    }
}

android {
    namespace = "com.mrl.pixiv.repository"
}