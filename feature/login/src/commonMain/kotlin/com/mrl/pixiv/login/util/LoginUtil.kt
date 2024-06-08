package com.mrl.pixiv.login.util

import coil3.Uri
import com.mrl.pixiv.common.viewmodel.auth.AuthAction
import io.ktor.utils.io.charsets.Charsets
import io.ktor.utils.io.core.toByteArray
import okio.ByteString.Companion.toByteString
import kotlin.io.encoding.Base64
import kotlin.random.Random

private var codeVerifier = getCodeVer()

internal fun getCodeVer(): String {
    val randomKeySet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
    val result = (0..128).map {
        randomKeySet[Random.nextInt(randomKeySet.length)]
    }.joinToString("")
    codeVerifier = result
    return codeVerifier
}

internal fun getCodeChallenge(): String =
    Base64.UrlSafe.encode(
        getCodeVer().toByteArray(Charsets.UTF_8).toByteString().sha256().toByteArray(),
    ).replace("=", "")

internal fun generateWebViewUrl(create: Boolean) =
    if (create) {
        "https://app-api.pixiv.net/web/v1/provisional-accounts/create?code_challenge=${getCodeChallenge()}&code_challenge_method=S256&client=pixiv-android"
    } else {
        "https://app-api.pixiv.net/web/v1/login?code_challenge=${getCodeChallenge()}&code_challenge_method=S256&client=pixiv-android"
    }

internal fun checkUri(dispatch: (AuthAction) -> Unit, uri: Uri): Boolean {
    if (uri.scheme == "pixiv" && uri.path == "/login") {
        val code = uri.getQueryParameter("code")
        code?.let { dispatch(AuthAction.Login(code, codeVerifier)) }
        return true
    }
    return false
}

private fun Uri.getQueryParameter(s: String): String? {
    this.query?.split('&')?.forEach {
        val pair = it.split("=")
        if (pair[0] == s) {
            return pair[1]
        }
    }
    return null
}
