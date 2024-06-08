rootProject.name = "PiPixiv"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        maven("https://jogamp.org/deployment/maven")
    }
    versionCatalogs {
        create("kotlinx") {
            from(files("gradle/kotlinx.versions.toml"))
        }
    }
}

include(":composeApp")
include(":common")
include(":common-viewmodel")
include(":data")
include(":data:processor")
include(":datasource")
include(":repository")
include(":util")

file("./feature").listFiles()?.filter { it.isDirectory }?.forEach { moduleDir ->
    // 使用目录名称构建模块路径
    val moduleName = ":feature:${moduleDir.name}"
    println("module name: $moduleName")
    include(moduleName)
}
include(":domain")
include(":feature:home")
include(":common-ui")
include(":feature:search")
include(":feature:profile")
include(":feature:picture")
include(":network")
