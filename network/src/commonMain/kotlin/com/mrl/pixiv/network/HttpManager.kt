package com.mrl.pixiv.network

import androidx.compose.ui.text.intl.Locale
import com.mrl.pixiv.common.coroutine.launchIO
import com.mrl.pixiv.data.setting.UserPreference
import com.mrl.pixiv.domain.auth.RefreshUserAccessTokenUseCase
import com.mrl.pixiv.repository.SettingRepository
import com.mrl.pixiv.repository.UserRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.URLProtocol
import io.ktor.utils.io.core.toByteArray
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate.Formats.ISO
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.alternativeParsing
import kotlinx.datetime.format.char
import kotlinx.datetime.offsetIn
import kotlinx.datetime.toLocalDateTime
import okio.ByteString.Companion.toByteString
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HttpManager(
    settingRepository: SettingRepository,
) : KoinComponent {
    private var allSetting: UserPreference = settingRepository.allSettingsSync
    private val userRepository: UserRepository by inject()
    private val refreshUserAccessTokenUseCase: RefreshUserAccessTokenUseCase by inject()

    init {
        launchIO {
            userRepository.userAccessToken.collect {
                token = it
            }
        }
    }

    private lateinit var token: String
    private val enableBypassSniffing: Boolean = allSetting.enableBypassSniffing
    private val imageHost: String = allSetting.imageHost.ifEmpty { IMAGE_HOST }


    companion object {
        private const val TAG = "HttpManager"
        private const val HashSalt =
            "28c1fdd170a5204386cb1313c7077b34f83e4aaf4aa829ce78c231e05b0bae2c"
        private const val API_HOST = "app-api.pixiv.net"
        private const val IMAGE_HOST = "i.pximg.net"
        private const val STATIC_IMAGE_HOST = "s.pximg.net"
        private const val AUTH_HOST = "oauth.secure.pixiv.net"
        private val hostMap: Map<String, String> = mapOf(
            API_HOST to "210.140.131.199",
            AUTH_HOST to "210.140.131.219",
            IMAGE_HOST to "210.140.92.144",
            STATIC_IMAGE_HOST to "210.140.92.143",
            "doh" to "doh.dns.sb",
        )

    }


    private fun encode(text: String): String {
        try {
            val md5 = text.toByteArray().toByteString().md5()
            return md5.hex()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

//    private val commonHeaderInterceptor by lazy {
//        Interceptor { chain ->
//            val requestBuilder = addAuthHeader(chain)
//            val request = requestBuilder
//                .header("Host", API_HOST)
//                .build()
//            val response = switchHostResponse(chain, request)
//            if (response.code in 400..499) {
//                response.close()
//                Log.d(TAG, "commonHeaderInterceptor: ${response.code}")
//                Log.d(TAG, "current Thread: ${Thread.currentThread().name}")
//                runBlocking(Dispatchers.IO) {
//                    Log.d(TAG, "current Thread: ${Thread.currentThread().name}")
//                    refreshUserAccessTokenUseCase {
//                        token = it
//                    }
//                }
//                val newRequest = addAuthHeader(chain).header("Host", API_HOST).build()
//                return@Interceptor switchHostResponse(chain, newRequest)
//            }
//            response
//        }
//    }

    private val iso8601DateTimeFormat = LocalDateTime.Format {
        date(ISO)
        alternativeParsing({
            char('t')
        }) {
            char('T')
        }
        hour()
        char(':')
        minute()
        char(':')
        second()
    }

    private fun addAuthHeader(request: HttpRequestBuilder) {
        val local = Locale.current
        val instantNow = Clock.System.now()
        val isoDate = "${
            instantNow.toLocalDateTime(TimeZone.currentSystemDefault())
                .format(iso8601DateTimeFormat)
        }${instantNow.offsetIn(TimeZone.currentSystemDefault())}"
        request.headers.apply {
            remove("User-Agent")
            set(
                "User-Agent",
                "PixivAndroidApp/5.0.166 (Android 14; 2210132C)"
            )
//            if (request.host != AUTH_HOST || request.host != hostMap[AUTH_HOST]) {
            set("Authorization", "Bearer $token")
//            }
            set("Accept-Language", "${local.language}_${local.region}")
            set("App-OS", "Android")
            set("App-OS-Version", "14")
            set("App-Version", "5.0.166")
            set("X-Client-Time", isoDate)
            set("X-Client-Hash", encode("$isoDate$HashSalt"))
//            set("Host", request.host)
        }

    }

    val imageHttpClient by lazy {
        baseImageHttpClient.apply {
            plugin(HttpSend).intercept { request ->
                request.apply {
                    url {
                        host = imageHost
                        protocol = URLProtocol.HTTPS
                    }
                    headers["Referer"] = "https://app-api.pixiv.net/"
                }
                execute(request)
            }
        }
    }

    val authHttpClient by lazy {
        baseHttpClient.apply {
            plugin(HttpSend).intercept { request ->
                addAuthHeader(request)
                request.apply {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = AUTH_HOST
                    }
                    headers.remove("Authorization")
                    headers["Host"] = AUTH_HOST
                }
                execute(request)
            }
            config {
                defaultRequest {
                    url("https://${if (enableBypassSniffing) AUTH_HOST else hostMap[AUTH_HOST]}")
                    accept(ContentType.Application.Json)
                }
            }
        }
    }

    val apiHttpClient by lazy {
        baseHttpClient.apply {
            plugin(HttpSend).intercept { request ->
                addAuthHeader(request)
                request.apply {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = API_HOST
                    }
                }
                execute(request)
            }
            config {
                defaultRequest {
                    url("https://${if (enableBypassSniffing) API_HOST else hostMap[API_HOST]}")
                    accept(ContentType.Application.Json)
                }
            }
        }
    }
}

expect val baseHttpClient: HttpClient

expect val baseImageHttpClient: HttpClient