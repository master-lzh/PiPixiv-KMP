package buildsrc

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension


fun Project.configureFeature(
    kmpExtension: KotlinMultiplatformExtension
) {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    val kotlinx = extensions.getByType<VersionCatalogsExtension>().named("kotlinx")
    with(kmpExtension) {
        sourceSets.commonMain.dependencies {
            // Project
            implementation(project(":repository"))
            implementation(project(":common-ui"))
            implementation(project(":common-viewmodel"))
            implementation(project(":repository"))
            // Decompose
            api(libs.findBundle("lifecycle").get())
//            implementation(libs.findBundle("mvikotlin").get())

            implementation(kotlinx.findBundle("serialization").get())

            // Coroutines
            implementation(project.dependencies.platform(kotlinx.findLibrary("coroutines-bom").get()))
            implementation(kotlinx.findLibrary("coroutines-core").get())
        }
    }
}