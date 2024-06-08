import buildsrc.configureAndroid
import buildsrc.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import buildsrc.configureKmp
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

class KmpApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.native.cocoapods")
                apply("com.android.application")
                apply("org.jetbrains.kotlin.plugin.compose")
            }
            val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
            configureKmp(kmpExtension)
            configureCompose(kmpExtension)
            val androidAppExtension= extensions.getByType<BaseAppModuleExtension>()
            configureAndroid(androidAppExtension)
        }
    }
}