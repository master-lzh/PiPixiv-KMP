package com.mrl.pixiv.data.auth

import com.mrl.pixiv.data.Constants
import com.mrl.pixiv.data.GenerateMap
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@GenerateMap
@Serializable
data class AuthTokenFieldReq(
    @SerialName("client_id")
    val clientId: String = Constants.CLIENT_ID,
    @SerialName("client_secret")
    val clientSecret: String = Constants.CLIENT_SECRET,
    @SerialName("grant_type")
    val grantType: String,

    val username: String? = null,
    val password: String? = null,

    val code: String? = null,
    @SerialName("redirect_uri")
    val redirectUri: String? = null,
    @SerialName("code_verifier")
    val codeVerifier: String? = null,

    @SerialName("refresh_token")
    val refreshToken: String? = null,

    @SerialName("get_secure_url")
    val getSecureUrl: Int = 1,
    @SerialName("include_policy")
    val includePolicy: Boolean = true,
)

enum class GrantType(val value: String) {
    PASSWORD("password"),
    REFRESH_TOKEN("refresh_token"),
    AUTHORIZATION_CODE("authorization_code")
}