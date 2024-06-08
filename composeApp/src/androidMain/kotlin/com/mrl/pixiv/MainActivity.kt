package com.mrl.pixiv

import App
import android.app.ActivityManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import coil3.ImageLoader
import coil3.asCoilImage
import coil3.gif.AnimatedImageDecoder
import coil3.gif.GifDecoder
import coil3.network.ktor.KtorNetworkFetcherFactory
import coil3.request.allowRgb565
import com.mrl.pixiv.image.CoilDiskCache
import com.mrl.pixiv.image.CoilMemoryCache
import com.mrl.pixiv.network.HttpManager
import com.mrl.pixiv.splash.viewmodel.SplashViewModel
import kotlinx.coroutines.flow.update
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModel()
    private val httpManager: HttpManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                splashViewModel.state.isLoading
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        setContent {
            val errorImage =
                AppCompatResources.getDrawable(this, R.drawable.ic_error_outline_24)?.asCoilImage()
            App(
                imageLoaderBuilder = {
                    ImageLoader.Builder(it)
                        .error(errorImage)
                        .allowRgb565(getSystemService<ActivityManager>()!!.isLowRamDevice)
                        .diskCache(CoilDiskCache.get(this))
                        .memoryCache(CoilMemoryCache.get(this))
                        .components {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                add(AnimatedImageDecoder.Factory())
                            } else {
                                add(GifDecoder.Factory())
                            }
                            add(KtorNetworkFetcherFactory(httpManager.imageHttpClient))
                        }
                },
                splashViewModel = splashViewModel
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        splashViewModel.intent.update {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }
}