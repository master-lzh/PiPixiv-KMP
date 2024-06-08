plugins {
    id("pixiv.kmp.application")
    alias(kotlinx.plugins.serialization)
}

kotlin {
    cocoapods {
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.com.google.android.material.material)
            implementation(libs.splashscreen)

            implementation(kotlinx.coroutines.android)
            implementation(kotlinx.ktor.client.okhttp)
        }
        commonMain.dependencies {
            // Project
            implementation(project(":common-viewmodel"))
            implementation(project(":data"))
            implementation(project(":datasource"))
            implementation(project(":domain"))
            implementation(project(":network"))
            implementation(project(":repository"))
            implementation(project(":util"))

            file("../feature").listFiles()?.filter { it.isDirectory }?.forEach { moduleDir ->
                // 使用目录名称构建模块路径
                val moduleName = ":feature:${moduleDir.name}"
                println("module name: $moduleName")
                implementation(project(moduleName))
            }

            // Coil3
            implementation(libs.coil3)
            implementation(libs.coil3.gif)
            // Compose
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            // Coroutines
            implementation(project.dependencies.platform(kotlinx.coroutines.bom))
            implementation(kotlinx.coroutines.core)

            // Ktor
            implementation(kotlinx.ktor.client.core)
            implementation(kotlinx.ktor.client.logging)
            implementation(kotlinx.ktor.client.content.negotiation)
            implementation(kotlinx.ktor.serialization.kotlinx.json)

            // Logging
            implementation(libs.kermit)

            implementation(kotlinx.datetime)
            // DataStore
            implementation(libs.bundles.datastore)
        }
        iosMain.dependencies {
            implementation(kotlinx.ktor.client.darwin)
        }
    }
}

android {
    namespace = "com.mrl.pixiv"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.mrl.pixiv"
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("../pipixiv.jks")
            storePassword = System.getenv("RELEASE_KEYSTORE_PASSWORD")
            keyAlias = System.getenv("RELEASE_KEYSTORE_ALIAS")
            keyPassword = System.getenv("RELEASE_KEY_PASSWORD")
        }
    }
    buildTypes {
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("release")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    buildFeatures {
        compose = true
    }
    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

