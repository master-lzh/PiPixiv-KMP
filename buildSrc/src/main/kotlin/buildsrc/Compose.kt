package buildsrc

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Retrieves the [compose][org.jetbrains.compose.ComposePlugin.Dependencies] extension.
 */
val KotlinMultiplatformExtension.compose: ComposePlugin.Dependencies
    get() =
        (this as org.gradle.api.plugins.ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies

/**
 * Configures the [compose][org.jetbrains.compose.ComposePlugin.Dependencies] extension.
 */
fun KotlinMultiplatformExtension.compose(configure: Action<ComposePlugin.Dependencies>): Unit =
    (this as org.gradle.api.plugins.ExtensionAware).extensions.configure("compose", configure)


fun Project.configureCompose(
    commonExtension: KotlinMultiplatformExtension,
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val kotlinx = extensions.getByType<VersionCatalogsExtension>().named("kotlinx")
    commonExtension.apply {
        sourceSets.commonMain.dependencies {
            // Compose
            println("implementation compose dependencies")
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.material3)
            implementation(compose.materialIconsExtended)

            // Navigation
            implementation(libs.findBundle("navigation").get())

            // Ktor
            implementation(kotlinx.findLibrary("ktor-client-core").get())
            implementation(kotlinx.findLibrary("ktor-client-logging").get())

            // Coil3
            implementation(libs.findLibrary("coil3-core").get())
            implementation(libs.findLibrary("coil3-compose").get())
            implementation(libs.findLibrary("coil3-network-ktor").get())

            implementation(kotlinx.findLibrary("collection-immutable").get())
        }
    }
}