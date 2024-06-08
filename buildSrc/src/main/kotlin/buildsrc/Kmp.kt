package buildsrc

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

val KotlinMultiplatformExtension.cocoapods: CocoapodsExtension
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("cocoapods") as CocoapodsExtension

fun KotlinMultiplatformExtension.cocoapods(configure: Action<CocoapodsExtension>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("cocoapods", configure)

@OptIn(ExperimentalKotlinGradlePluginApi::class)
fun Project.configureKmp(
    extension: KotlinMultiplatformExtension
) {
    with(extension) {
        compilerOptions {
            freeCompilerArgs.addAll(
                listOf(
                    "-opt-in=kotlin.RequiresOptIn",
                    // Enable experimental coroutines APIs, including Flow
                    "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                    "-opt-in=kotlinx.coroutines.FlowPreview",
                    "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                    "-opt-in=androidx.compose.material.ExperimentalMaterialApi",
                    "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                    "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                    "-opt-in=androidx.compose.animation.ExperimentalAnimationApi",
                    "-opt-in=androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi",
                    "-opt-in=androidx.compose.animation.ExperimentalSharedTransitionApi",
                    "-opt-in=coil3.annotation.ExperimentalCoilApi",
                    "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
                    "-opt-in=org.koin.core.annotation.KoinExperimentalAPI",
                    "-opt-in=kotlinx.cinterop.ExperimentalForeignApi",
                    "-opt-in=kotlin.io.encoding.ExperimentalEncodingApi",
                    "-Xexpect-actual-classes",
                )
            )
        }

        androidTarget {
            compilerOptions {
                jvmTarget.set(JvmTarget.JVM_11)
                // Treat all Kotlin warnings as errors (disabled by default)
                // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties
                val warningsAsErrors: String? by project
                allWarningsAsErrors.set(warningsAsErrors.toBoolean())
            }
        }

        iosX64()
        iosArm64()
        iosSimulatorArm64()

        cocoapods {
            summary = "Some description for the Shared Module"
            homepage = "Link to the Shared Module homepage"
            version = "1.0"
            ios.deploymentTarget = "16.0"
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        sourceSets.androidMain {
            dependencies {
                implementation(libs.findLibrary("koin-android").get())
            }
        }
        sourceSets.commonMain {
            dependencies {
                val baseModules = listOf("common", "data", "util")
                if (this@configureKmp.name !in baseModules) {
                    implementation(project(":common"))
                    implementation(project(":data"))
                }
                // Resources
                implementation(compose.components.resources)
                // Koin
                implementation(project.dependencies.platform(libs.findLibrary("koin-bom").get()))
                implementation(libs.findLibrary("koin-core").get())
                implementation(libs.findLibrary("koin-compose").get())
                implementation(libs.findLibrary("koin-compose-viewmodel").get())

                // Kermit Logging
                implementation(libs.findLibrary("kermit").get())
            }
        }
    }
}