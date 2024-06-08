package com.mrl.pixiv.navigation.main

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mrl.pixiv.common.lifecycle.collectAsStateWithLifecycle
import com.mrl.pixiv.common.router.Destination
import com.mrl.pixiv.common.router.Graph
import com.mrl.pixiv.common.ui.LocalNavigator
import com.mrl.pixiv.common.ui.currentOrThrow
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkViewModel
import com.mrl.pixiv.common.viewmodel.follow.FollowViewModel
import com.mrl.pixiv.common.viewmodel.illust.IllustViewModel
import com.mrl.pixiv.home.HomeScreen
import com.mrl.pixiv.home.viewmodel.HomeViewModel
import com.mrl.pixiv.picture.PictureDeeplinkScreen
import com.mrl.pixiv.picture.PictureScreen
import com.mrl.pixiv.profile.ProfileScreen
import com.mrl.pixiv.profile.detail.OtherProfileDetailScreen
import com.mrl.pixiv.profile.detail.SelfProfileDetailScreen
import com.mrl.pixiv.search.OutsideSearchResultsScreen
import com.mrl.pixiv.search.SearchResultScreen
import com.mrl.pixiv.search.SearchScreen
import com.mrl.pixiv.search.preview.SearchPreviewScreen
import com.mrl.pixiv.search.viewmodel.SearchViewModel
import com.mrl.pixiv.splash.viewmodel.SplashViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainGraph(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = LocalNavigator.currentOrThrow
) {
    val homeViewModel: HomeViewModel = koinViewModel()
    val followViewModel: FollowViewModel = koinViewModel()
    val bookmarkViewModel: BookmarkViewModel = koinViewModel()
    val illustViewModel: IllustViewModel = koinViewModel()
    val splashViewModel: SplashViewModel =
        koinViewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current!!)
    val intent = splashViewModel.intent.collectAsStateWithLifecycle().value
    HandleDeeplink(intent, navHostController)
    NavHost(
        navController = navHostController,
        route = Graph.MAIN,
        startDestination = Destination.HomeScreen.route,
    ) {
        // 首页
        composable(
            route = Destination.HomeScreen.route,
        ) {
            HomeScreen(
                modifier = modifier,
                homeViewModel = homeViewModel,
                bookmarkViewModel = bookmarkViewModel,
            )
        }

        // 搜索预览页
        composable(
            route = Destination.SearchPreviewScreen.route,
        ) {
            SearchPreviewScreen(
                modifier = modifier,
            )
        }

        // 个人主页
        composable(
            route = Destination.ProfileScreen.route,
        ) {
            ProfileScreen(
                modifier = modifier,
            )
        }

        // 个人详情页
        composable(
            route = Destination.SelfProfileDetailScreen.route,
        ) {
            SelfProfileDetailScreen(
                bookmarkViewModel = bookmarkViewModel
            )
        }

        // 他人详情页
        composable(
            route = "${Destination.OtherProfileDetailScreen.route}/{${Destination.OtherProfileDetailScreen.userId}}",
            arguments = listOf(
                navArgument(Destination.OtherProfileDetailScreen.userId) {
                    defaultValue = 0L
                }
            ),
        ) {
            OtherProfileDetailScreen(
                uid = it.arguments?.getLong(Destination.OtherProfileDetailScreen.userId)
                    ?: 0L,
                bookmarkViewModel = bookmarkViewModel
            )
        }

        // 作品详情页
        composable(
            route = "${Destination.PictureScreen.route}/{${Destination.PictureScreen.illustId}}?prefix={${Destination.PictureScreen.prefix}}",
            arguments = listOf(
                navArgument(Destination.PictureScreen.illustId) {
                    defaultValue = 0L
                },
                navArgument(Destination.PictureScreen.prefix) {
                    defaultValue = ""
                }
            ),
            enterTransition = { scaleIn(initialScale = 0.9f) + fadeIn() },
            exitTransition = { scaleOut(targetScale = 1.1f) + fadeOut() },
            popEnterTransition = { scaleIn(initialScale = 1.1f) + fadeIn() },
            popExitTransition = { scaleOut(targetScale = 0.9f) + fadeOut() },
        ) {
            val illustId = (it.arguments?.getLong(Destination.PictureScreen.illustId)) ?: 0L
            val illust = illustViewModel.state.illusts[illustId]
//            val prefix = it.arguments?.getString(Destination.PictureScreen.prefix) ?: ""
            if (illust != null) {
                PictureScreen(
                    illust = illust,
                    bookmarkViewModel = bookmarkViewModel,
                    followViewModel = followViewModel,
                )
            } else {
                PictureDeeplinkScreen(
                    illustId = illustId,
                    bookmarkViewModel = bookmarkViewModel,
                    followViewModel = followViewModel,
                )
            }
        }

        composable(
            route = "${Destination.PictureDeeplinkScreen.route}/{${Destination.PictureDeeplinkScreen.illustId}}",
            arguments = listOf(
                navArgument(Destination.PictureDeeplinkScreen.illustId) {
                    defaultValue = 0L
                }
            ),
        ) {
            val illustId =
                it.arguments?.getLong(Destination.PictureDeeplinkScreen.illustId) ?: 0L
            PictureDeeplinkScreen(
                illustId = illustId,
                bookmarkViewModel = bookmarkViewModel,
                followViewModel = followViewModel,
            )
        }

        // 搜索页
        composable(
            route = Destination.SearchScreen.route,
        ) {
            val searchViewModel: SearchViewModel = koinViewModel(viewModelStoreOwner = it)
            val searchNavHostController = rememberNavController()
            CompositionLocalProvider(
                LocalNavigator provides searchNavHostController,
            ) {
                NavHost(
                    navController = searchNavHostController,
                    route = Graph.SEARCH,
                    startDestination = Destination.SearchScreen.route
                ) {
                    composable(
                        route = Destination.SearchScreen.route,
                    ) {
                        SearchScreen(
                            navHostController = navHostController,
                            searchViewModel = searchViewModel
                        )
                    }
                    composable(
                        route = Destination.SearchResultsScreen.route,
                    ) {
                        SearchResultScreen(
                            bookmarkViewModel = bookmarkViewModel,
                            searchViewModel = searchViewModel,
                            navHostController = navHostController
                        )
                    }
                }
            }
        }

        // 外部搜索结果页
        composable(
            route = "${Destination.SearchResultsScreen.route}/{${Destination.SearchResultsScreen.searchWord}}",
            arguments = listOf(
                navArgument(Destination.SearchResultsScreen.searchWord) {
                    defaultValue = ""
                }
            ),
        ) {
            val searchWord =
                (it.arguments?.getString(Destination.SearchResultsScreen.searchWord))
                    ?: ""
            OutsideSearchResultsScreen(
                searchWord = searchWord,
                bookmarkViewModel = bookmarkViewModel,
            )
        }

//        // 设置页
//        composable(
//            route = Destination.SettingScreen.route,
//        ) {
//            val settingViewModel: SettingViewModel =
//                koinViewModel(viewModelStoreOwner = LocalViewModelStoreOwner.current)
//            val settingNavHostController = rememberNavController()
//            CompositionLocalProvider(LocalNavigator provides settingNavHostController) {
//                NavHost(
//                    navController = settingNavHostController,
//                    startDestination = Destination.SettingScreen.route
//                ) {
//                    composable(
//                        route = Destination.SettingScreen.route,
//                    ) {
//                        SettingScreen(
//                            viewModel = settingViewModel,
//                            mainNavHostController = navHostController
//                        )
//                    }
//
//                    // 网络设置页
//                    composable(
//                        route = Destination.NetworkSettingScreen.route,
//                    ) {
//                        NetworkSettingScreen(viewModel = settingViewModel)
//                    }
//                }
//            }
//        }
//
//        // 历史记录
//        composable(
//            route = Destination.HistoryScreen.route,
//        ) {
//            HistoryScreen(
//                modifier = modifier,
//            )
//        }
//
//        // 本人收藏页
//        composable(
//            route = Destination.SelfCollectionScreen.route,
//        ) {
//            SelfCollectionScreen(
//                modifier = modifier,
//                bookmarkViewModel = bookmarkViewModel
//            )
//        }
    }

}

expect class Intent

@Composable
expect fun HandleDeeplink(
    intent: Intent?,
    navHostController: NavHostController
)