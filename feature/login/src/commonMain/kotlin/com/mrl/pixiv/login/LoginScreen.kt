package com.mrl.pixiv.login

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import coil3.toUri
import com.mrl.pixiv.common.lifecycle.OnLifecycle
import com.mrl.pixiv.common.router.Graph
import com.mrl.pixiv.common.ui.LocalNavigator
import com.mrl.pixiv.common.ui.Screen
import com.mrl.pixiv.common.ui.currentOrThrow
import com.mrl.pixiv.common.viewmodel.auth.AuthAction
import com.mrl.pixiv.common.viewmodel.auth.AuthState
import com.mrl.pixiv.login.util.checkUri
import com.mrl.pixiv.login.util.generateWebViewUrl
import com.mrl.pixiv.login.viewmodel.LoginViewModel
import com.multiplatform.webview.request.RequestInterceptor
import com.multiplatform.webview.request.WebRequest
import com.multiplatform.webview.request.WebRequestInterceptResult
import com.multiplatform.webview.web.LoadingState
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewNavigator
import com.multiplatform.webview.web.rememberWebViewNavigator
import com.multiplatform.webview.web.rememberWebViewState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import pipixiv.feature.login.generated.resources.Res
import pipixiv.feature.login.generated.resources.sign_in
import pipixiv.feature.login.generated.resources.sign_up

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = koinViewModel(),
    navHostController: NavHostController = LocalNavigator.currentOrThrow,
) {
    OnLifecycle(onLifecycle = loginViewModel::onStart)
    LoginScreen(
        modifier = modifier,
        state = loginViewModel.state,
        navToMainGraph = {
            navHostController.popBackStack()
            navHostController.navigate(Graph.MAIN)
        },
        dispatch = loginViewModel::dispatch,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LoginScreen(
    modifier: Modifier = Modifier,
    state: AuthState,
    navToMainGraph: () -> Unit = {},
    dispatch: (AuthAction) -> Unit,
) {
    var currUrl by rememberSaveable { mutableStateOf(generateWebViewUrl(true)) }
    LaunchedEffect(state.isLogin) {
        if (state.isLogin) {
            navToMainGraph()
        }
    }
    Screen(
        modifier = modifier
            .statusBarsPadding()
            .imePadding(),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Button(onClick = {
                        currUrl = generateWebViewUrl(false)
                    }) {
                        Text(text = stringResource(Res.string.sign_in))
                    }
                    Button(onClick = {
                        currUrl = generateWebViewUrl(true)
                    }) {
                        Text(text = stringResource(Res.string.sign_up))
                    }
                }
            )
        }
    ) {
        val webViewState = rememberWebViewState(url = currUrl)
        val navigator = rememberWebViewNavigator(
            requestInterceptor = object :RequestInterceptor {
                override fun onInterceptUrlRequest(
                    request: WebRequest,
                    navigator: WebViewNavigator
                ): WebRequestInterceptResult {
                    if (checkUri(dispatch, request.url.toUri(),)) {
                        return WebRequestInterceptResult.Reject
                    }
                    return WebRequestInterceptResult.Allow
                }
            }
        )
        when (webViewState.loadingState) {
            LoadingState.Finished -> {}

            LoadingState.Initializing -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            is LoadingState.Loading -> LinearProgressIndicator(
                progress = {
                    (webViewState.loadingState as LoadingState.Loading).progress
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        WebView(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            state = webViewState,
            navigator = navigator,
            onCreated = {
                webViewState.webSettings.apply {
                    isJavaScriptEnabled = true

                }
            },
//            client = object : AccompanistWebViewClient() {
//                override fun shouldOverrideUrlLoading(
//                    view: WebView?,
//                    request: WebResourceRequest?
//                ): Boolean {
//                    if (checkUri(dispatch, request?.url!!)) {
//                        return true
//                    }
//                    return super.shouldOverrideUrlLoading(view, request)
//                }
//            }
        )
    }
}