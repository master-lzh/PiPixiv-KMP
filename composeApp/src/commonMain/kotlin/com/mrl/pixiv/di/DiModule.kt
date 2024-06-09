package com.mrl.pixiv.di

import com.mrl.pixiv.common.data.DispatcherEnum
import com.mrl.pixiv.common.data.HttpClientEnum
import com.mrl.pixiv.common.viewmodel.auth.AuthMiddleware
import com.mrl.pixiv.common.viewmodel.auth.AuthReducer
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkMiddleware
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkReducer
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkViewModel
import com.mrl.pixiv.common.viewmodel.follow.FollowMiddleware
import com.mrl.pixiv.common.viewmodel.follow.FollowReducer
import com.mrl.pixiv.common.viewmodel.follow.FollowViewModel
import com.mrl.pixiv.common.viewmodel.illust.IllustMiddleware
import com.mrl.pixiv.common.viewmodel.illust.IllustReducer
import com.mrl.pixiv.common.viewmodel.illust.IllustViewModel
import com.mrl.pixiv.datasource.local.datastore.SearchDataSource
import com.mrl.pixiv.datasource.local.datastore.SettingDataSource
import com.mrl.pixiv.datasource.local.datastore.UserAuthDataSource
import com.mrl.pixiv.datasource.local.datastore.UserInfoDataSource
import com.mrl.pixiv.datasource.local.datastore.searchDataStore
import com.mrl.pixiv.datasource.local.datastore.userAuthDataStore
import com.mrl.pixiv.datasource.local.datastore.userInfoDataStore
import com.mrl.pixiv.datasource.local.datastore.userPreferenceDataStore
import com.mrl.pixiv.datasource.remote.IllustHttpService
import com.mrl.pixiv.datasource.remote.SearchHttpService
import com.mrl.pixiv.datasource.remote.TrendingHttpService
import com.mrl.pixiv.datasource.remote.UgoiraHttpService
import com.mrl.pixiv.datasource.remote.UserAuthHttpService
import com.mrl.pixiv.datasource.remote.UserHttpService
import com.mrl.pixiv.domain.GetLocalUserInfoUseCase
import com.mrl.pixiv.domain.HasShowBookmarkTipUseCase
import com.mrl.pixiv.domain.SetLocalUserInfoUseCase
import com.mrl.pixiv.domain.SetShowBookmarkTipUseCase
import com.mrl.pixiv.domain.SetUserAccessTokenUseCase
import com.mrl.pixiv.domain.SetUserRefreshTokenUseCase
import com.mrl.pixiv.domain.auth.RefreshUserAccessTokenUseCase
import com.mrl.pixiv.domain.illust.GetIllustBookmarkDetailUseCase
import com.mrl.pixiv.domain.setting.GetAppThemeUseCase
import com.mrl.pixiv.home.viewmodel.HomeMiddleware
import com.mrl.pixiv.home.viewmodel.HomeReducer
import com.mrl.pixiv.home.viewmodel.HomeViewModel
import com.mrl.pixiv.login.viewmodel.LoginViewModel
import com.mrl.pixiv.network.HttpManager
import com.mrl.pixiv.picture.viewmodel.PictureDeeplinkViewModel
import com.mrl.pixiv.picture.viewmodel.PictureMiddleware
import com.mrl.pixiv.picture.viewmodel.PictureReducer
import com.mrl.pixiv.picture.viewmodel.PictureViewModel
import com.mrl.pixiv.profile.detail.viewmodel.ProfileDetailMiddleware
import com.mrl.pixiv.profile.detail.viewmodel.ProfileDetailReducer
import com.mrl.pixiv.profile.detail.viewmodel.ProfileDetailViewModel
import com.mrl.pixiv.profile.viewmodel.ProfileMiddleware
import com.mrl.pixiv.profile.viewmodel.ProfileReducer
import com.mrl.pixiv.profile.viewmodel.ProfileViewModel
import com.mrl.pixiv.repository.AuthRepository
import com.mrl.pixiv.repository.CollectionRepository
import com.mrl.pixiv.repository.HistoryRepository
import com.mrl.pixiv.repository.IllustRepository
import com.mrl.pixiv.repository.SearchRepository
import com.mrl.pixiv.repository.SettingRepository
import com.mrl.pixiv.repository.TrendingRepository
import com.mrl.pixiv.repository.UserRepository
import com.mrl.pixiv.search.preview.viewmodel.SearchPreviewMiddleware
import com.mrl.pixiv.search.preview.viewmodel.SearchPreviewReducer
import com.mrl.pixiv.search.preview.viewmodel.SearchPreviewViewModel
import com.mrl.pixiv.search.viewmodel.SearchMiddleware
import com.mrl.pixiv.search.viewmodel.SearchReducer
import com.mrl.pixiv.search.viewmodel.SearchViewModel
import com.mrl.pixiv.splash.viewmodel.SplashMiddleware
import com.mrl.pixiv.splash.viewmodel.SplashReducer
import com.mrl.pixiv.splash.viewmodel.SplashViewModel
import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.serialization.json.Json
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val JSON = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
    isLenient = true
}

private val appModule = module {
    singleOf(::JSON)

    singleOf(::HttpManager)

    single(named(HttpClientEnum.API)) { provideApiHttpClient(get()) }

    single(named(HttpClientEnum.AUTH)) { get<HttpManager>().authHttpClient }

    single(named(DispatcherEnum.IO)) { Dispatchers.IO }

    single(named(DispatcherEnum.MAIN)) { Dispatchers.Main.immediate }
}

private val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::ProfileDetailViewModel)
    viewModelOf(::PictureViewModel)
    viewModelOf(::PictureDeeplinkViewModel)
    viewModelOf(::BookmarkViewModel)
    viewModelOf(::FollowViewModel)
    viewModelOf(::SearchViewModel)
    viewModelOf(::SearchPreviewViewModel)
//    viewModelOf(::SettingViewModel)
//    viewModelOf(::HistoryViewModel)
//    viewModelOf(::CollectionViewModel)
    viewModelOf(::IllustViewModel)
}

private val repositoryModule = module {
    singleOf(::UserRepository)
    singleOf(::SettingRepository)


    singleOf(::AuthRepository)
    singleOf(::IllustRepository)
    singleOf(::SearchRepository)
    singleOf(::TrendingRepository)
    singleOf(::HistoryRepository)
    singleOf(::CollectionRepository)
}

private val datasourceModule = module {
    single { UserAuthDataSource(userAuthDataStore) }
    single { UserInfoDataSource(userInfoDataStore) }
    single { SearchDataSource(searchDataStore) }
    single { SettingDataSource(userPreferenceDataStore) }

    single { IllustHttpService(get(named(HttpClientEnum.API))) }
    single { UserAuthHttpService((get(named(HttpClientEnum.AUTH)))) }
    single { UserHttpService(get(named(HttpClientEnum.API))) }
    single { SearchHttpService(get(named(HttpClientEnum.API))) }
    single { TrendingHttpService(get(named(HttpClientEnum.API))) }
    single { UgoiraHttpService(get(named(HttpClientEnum.API))) }
}

private val useCaseModule = module {
    singleOf(::SetUserRefreshTokenUseCase)
    singleOf(::SetUserAccessTokenUseCase)
    singleOf(::GetLocalUserInfoUseCase)
    singleOf(::SetLocalUserInfoUseCase)
    singleOf(::RefreshUserAccessTokenUseCase)
    singleOf(::GetAppThemeUseCase)
    singleOf(::GetIllustBookmarkDetailUseCase)
    singleOf(::HasShowBookmarkTipUseCase)
    singleOf(::SetShowBookmarkTipUseCase)
}

private val middlewareModule = module {
    factoryOf(::SplashMiddleware)
    factoryOf(::HomeMiddleware)
    factoryOf(::BookmarkMiddleware)
    factoryOf(::AuthMiddleware)
    factoryOf(::ProfileMiddleware)
    factoryOf(::ProfileDetailMiddleware)
    factoryOf(::PictureMiddleware)
    factoryOf(::FollowMiddleware)
    factoryOf(::SearchMiddleware)
    factoryOf(::SearchPreviewMiddleware)
//    factoryOf(::SettingMiddleware)
//    factoryOf(::HistoryMiddleware)
//    factoryOf(::CollectionMiddleware)
    factoryOf(::IllustMiddleware)
}

private val reducerModule = module {
    singleOf(::SplashReducer)
    singleOf(::HomeReducer)
    singleOf(::BookmarkReducer)
    singleOf(::AuthReducer)
    singleOf(::ProfileReducer)
    singleOf(::ProfileDetailReducer)
    singleOf(::PictureReducer)
    singleOf(::FollowReducer)
    singleOf(::SearchReducer)
    singleOf(::SearchPreviewReducer)
//    singleOf(::SettingReducer)
//    singleOf(::HistoryReducer)
//    singleOf(::CollectionReducer)
    singleOf(::IllustReducer)
}

val allModule = listOf(
    appModule,
    viewModelModule,
    repositoryModule,
    datasourceModule,
    useCaseModule,
    middlewareModule,
    reducerModule
)

fun provideApiHttpClient(
    httpManager: HttpManager
): HttpClient = httpManager.apiHttpClient
