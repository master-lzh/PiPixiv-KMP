plugins {
    id("pixiv.kmp.library")
    alias(kotlinx.plugins.serialization)
}

kotlin {
    cocoapods {
        framework {
            baseName = "datasource"
            isStatic = true
        }
    }
    
    sourceSets {
        commonMain.dependencies {
            implementation(project(":data"))
            implementation(project(":util"))

            implementation(kotlinx.bundles.serialization)
            implementation(libs.bundles.datastore)
            implementation(libs.bundles.okio)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.datasource"
}
