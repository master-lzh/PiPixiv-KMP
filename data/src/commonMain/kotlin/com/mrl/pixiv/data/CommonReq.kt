package com.mrl.pixiv.data

import androidx.annotation.StringDef
import kotlinx.serialization.json.Json

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class GenerateMap

enum class Filter(override val value: String) : IBaseEnum {
    ANDROID("for_android"),
    IOS("for_ios"),
}


interface IBaseEnum {
    val value: Any
}

@StringDef(Restrict.PUBLIC, Restrict.PRIVATE, Restrict.ALL)
annotation class Restrict {
    companion object {
        const val PUBLIC = "public"
        const val PRIVATE = "private"
        const val ALL = "all"
    }
}

internal val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
}
