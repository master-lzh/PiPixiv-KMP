import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.compose.setSingletonImageLoaderFactory
import com.mrl.pixiv.common.lifecycle.OnLifecycle
import com.mrl.pixiv.navigation.root.RootNavigationGraph
import com.mrl.pixiv.splash.viewmodel.SplashViewModel
import com.mrl.pixiv.theme.PiPixivTheme
import com.mrl.pixiv.util.Platform
import com.mrl.pixiv.util.platform
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pipixiv.composeapp.generated.resources.Res
import pipixiv.composeapp.generated.resources.ic_launch


@Composable
@Preview
fun App(
    imageLoaderBuilder: (PlatformContext) -> ImageLoader.Builder = { ImageLoader.Builder(it) },
    splashViewModel: SplashViewModel = koinViewModel(),
) {
    setSingletonImageLoaderFactory { context ->
        imageLoaderBuilder(context)
            // Coil spawns a new thread for every image load by default
            .fetcherDispatcher(Dispatchers.IO.limitedParallelism(8))
            .decoderDispatcher(Dispatchers.IO.limitedParallelism(2))
            .build()
    }
    OnLifecycle(onLifecycle = splashViewModel::onStart)
    PiPixivTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            if (platform == Platform.IOS && splashViewModel.state.startDestination == null) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val launchIcon = vectorResource(Res.drawable.ic_launch)
                    Image(
                        imageVector = launchIcon,
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(150.dp)
                    )
                }
            }
            splashViewModel.state.startDestination?.let {
                RootNavigationGraph(
                    navHostController = rememberNavController(),
                    startDestination = it
                )
            }
        }
    }
}