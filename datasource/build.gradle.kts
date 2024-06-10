plugins {
    id("pixiv.kmp.library")
    alias(kotlinx.plugins.serialization)
    alias(kotlinx.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    cocoapods {
        framework {
            baseName = "datasource"
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
            implementation(project(":data"))
            implementation(project(":util"))

            implementation(kotlinx.bundles.serialization)
            implementation(libs.bundles.datastore)
            implementation(libs.bundles.okio)
            implementation(libs.bundles.room)
        }
    }
}

android {
    namespace = "com.mrl.pixiv.datasource"
}

dependencies {
    kspCommonMainMetadata(libs.androidx.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}