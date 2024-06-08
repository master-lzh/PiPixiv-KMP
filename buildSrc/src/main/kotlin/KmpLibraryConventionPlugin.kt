import buildsrc.configureAndroid
import buildsrc.configureCompose
import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import buildsrc.configureKmp


class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.native.cocoapods")
                apply("org.jetbrains.compose")
                apply("com.android.library")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
            configureKmp(kmpExtension)
            configureCompose(kmpExtension)
            val androidExtension= extensions.getByType<LibraryExtension>()
            configureAndroid(androidExtension)
        }
    }
}