
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.asSkiaBitmap
import androidx.compose.ui.window.ComposeUIViewController
import coil3.ImageLoader
import coil3.asCoilImage
import coil3.network.ktor.KtorNetworkFetcherFactory
import com.mrl.pixiv.common.ui.LocalSystemTheme
import com.mrl.pixiv.image.CoilDiskCache
import com.mrl.pixiv.image.CoilMemoryCache
import com.mrl.pixiv.network.HttpManager
import org.jetbrains.compose.resources.imageResource
import org.koin.compose.koinInject
import pipixiv.composeapp.generated.resources.Res
import pipixiv.composeapp.generated.resources.ic_error_outline_48

fun MainViewController() = ComposeUIViewController {
    val httpManager: HttpManager = koinInject()
    CompositionLocalProvider(LocalSystemTheme provides isSystemInDarkTheme()) {
        val errorImage =
            imageResource(Res.drawable.ic_error_outline_48).asSkiaBitmap().asCoilImage()
        App(
            imageLoaderBuilder = {
                ImageLoader.Builder(it)
                    .error(errorImage)
                    .diskCache(CoilDiskCache.get(it))
                    .memoryCache(CoilMemoryCache.get(it))
                    .components {
                        add(KtorNetworkFetcherFactory(httpManager.imageHttpClient))
                    }
            },
        )
    }
}