plugins {
    id("pixiv.kmp.library")
    alias(kotlinx.plugins.serialization)
    id("kotlin-parcelize")
    alias(kotlinx.plugins.ksp)
}

kotlin {
    cocoapods {
        framework {
            baseName = "data"
            isStatic = true
        }
    }

    sourceSets {
        commonMain {
            kotlin {
                srcDirs(layout.buildDirectory.dir("generated/ksp/metadata/${this@commonMain.name}/kotlin"))
            }
        }
        commonMain.dependencies {
            //put your multiplatform dependencies here
            implementation(kotlinx.bundles.serialization)
            implementation(kotlinx.reflect)
            implementation(libs.bundles.datastore)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.data"
}

dependencies {
    kspCommonMainMetadata(project(":data:processor"))
}