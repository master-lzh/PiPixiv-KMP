plugins {
    id("pixiv.kmp.library")
}

kotlin {
    cocoapods {
        framework {
            baseName = "common"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(project(":util"))

            implementation(libs.bundles.lifecycle)
            implementation(libs.bundles.viewmodel)
            implementation(libs.bundles.navigation)
            implementation(libs.bundles.coil3)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.common"
}
