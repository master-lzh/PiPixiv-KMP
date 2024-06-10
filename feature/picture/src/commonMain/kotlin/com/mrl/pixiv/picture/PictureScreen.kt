package com.mrl.pixiv.picture

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.navigation.NavHostController
import co.touchlab.kermit.Logger
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import com.mohamedrejeb.calf.permissions.ExperimentalPermissionsApi
import com.mohamedrejeb.calf.permissions.Permission
import com.mohamedrejeb.calf.permissions.PermissionStatus
import com.mohamedrejeb.calf.permissions.rememberPermissionState
import com.mrl.pixiv.common.coroutine.launchNetwork
import com.mrl.pixiv.common.lifecycle.OnLifecycle
import com.mrl.pixiv.common.lifecycle.collectAsStateWithLifecycle
import com.mrl.pixiv.common.ui.LocalNavigator
import com.mrl.pixiv.common.ui.Orientation
import com.mrl.pixiv.common.ui.Screen
import com.mrl.pixiv.common.ui.components.TextSnackbar
import com.mrl.pixiv.common.ui.components.UserAvatar
import com.mrl.pixiv.common.ui.components.m3.Surface
import com.mrl.pixiv.common.ui.currentOrThrow
import com.mrl.pixiv.common.ui.deepBlue
import com.mrl.pixiv.common.ui.getOrientation
import com.mrl.pixiv.common.ui.getScreenHeight
import com.mrl.pixiv.common.ui.getScreenWidth
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkAction
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkState
import com.mrl.pixiv.common.viewmodel.bookmark.BookmarkViewModel
import com.mrl.pixiv.common.viewmodel.follow.FollowAction
import com.mrl.pixiv.common.viewmodel.follow.FollowState
import com.mrl.pixiv.common.viewmodel.follow.FollowViewModel
import com.mrl.pixiv.common_ui.item.SquareIllustItem
import com.mrl.pixiv.common_ui.util.navigateToOtherProfileDetailScreen
import com.mrl.pixiv.common_ui.util.navigateToOutsideSearchResultScreen
import com.mrl.pixiv.common_ui.util.navigateToPictureScreen
import com.mrl.pixiv.common_ui.util.popBackToMainScreen
import com.mrl.pixiv.data.Illust
import com.mrl.pixiv.data.Type
import com.mrl.pixiv.picture.components.UgoiraPlayer
import com.mrl.pixiv.picture.util.createShareImage
import com.mrl.pixiv.picture.util.rememberShareLauncher
import com.mrl.pixiv.picture.util.share
import com.mrl.pixiv.picture.viewmodel.PictureAction
import com.mrl.pixiv.picture.viewmodel.PictureDeeplinkViewModel
import com.mrl.pixiv.picture.viewmodel.PictureState
import com.mrl.pixiv.picture.viewmodel.PictureViewModel
import com.mrl.pixiv.repository.IllustRepository
import com.mrl.pixiv.util.OnScrollToBottom
import com.mrl.pixiv.util.calculateImageSize
import com.mrl.pixiv.util.convertUtcStringToLocalDateTime
import com.mrl.pixiv.util.format
import com.mrl.pixiv.util.isEven
import com.mrl.pixiv.util.queryParams
import com.mrl.pixiv.util.throttleClick
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import pipixiv.feature.picture.generated.resources.Res
import pipixiv.feature.picture.generated.resources.download_with_size
import pipixiv.feature.picture.generated.resources.follow
import pipixiv.feature.picture.generated.resources.followed
import pipixiv.feature.picture.generated.resources.liked
import pipixiv.feature.picture.generated.resources.related_artworks
import pipixiv.feature.picture.generated.resources.share
import pipixiv.feature.picture.generated.resources.sharing_success
import pipixiv.feature.picture.generated.resources.unknown_error
import pipixiv.feature.picture.generated.resources.viewed


@Composable
fun PictureScreen(
    modifier: Modifier = Modifier,
    illust: Illust,
    navHostController: NavHostController = LocalNavigator.currentOrThrow,
    pictureViewModel: PictureViewModel = koinViewModel { parametersOf(illust) },
    bookmarkViewModel: BookmarkViewModel,
    followViewModel: FollowViewModel,
) {
    OnLifecycle(onLifecycle = pictureViewModel::onCreate)
    val exception = pictureViewModel.exception.collectAsStateWithLifecycle(
        initialValue = null,
        lifecycle = LocalLifecycleOwner.current.lifecycle,
    ).value
    PictureScreen(
        modifier = modifier,
        state = pictureViewModel.state,
        exception = exception,
        bookmarkState = bookmarkViewModel.state,
        followState = followViewModel.state,
        illust = illust,
        navToPictureScreen = navHostController::navigateToPictureScreen,
        popBackStack = navHostController::popBackStack,
        dispatch = pictureViewModel::dispatch,
        bookmarkDispatch = bookmarkViewModel::dispatch,
        followDispatch = followViewModel::dispatch,
        navToSearchResultScreen = navHostController::navigateToOutsideSearchResultScreen,
        popBackToHomeScreen = navHostController::popBackToMainScreen,
        navToUserDetailScreen = navHostController::navigateToOtherProfileDetailScreen,
    )
}

@Composable
fun PictureDeeplinkScreen(
    modifier: Modifier = Modifier,
    illustId: Long,
    navHostController: NavHostController = LocalNavigator.currentOrThrow,
    pictureViewModel: PictureDeeplinkViewModel = koinViewModel { parametersOf(illustId) },
    bookmarkViewModel: BookmarkViewModel,
    followViewModel: FollowViewModel,
) {
    val illust = pictureViewModel.state.illust
    val exception = pictureViewModel.exception.collectAsStateWithLifecycle(
        initialValue = null,
        lifecycle = LocalLifecycleOwner.current.lifecycle,
    ).value
    if (illust != null) {
        PictureScreen(
            modifier = modifier,
            state = pictureViewModel.state,
            exception = exception,
            bookmarkState = bookmarkViewModel.state,
            followState = followViewModel.state,
            illust = illust,
            navToPictureScreen = navHostController::navigateToPictureScreen,
            popBackStack = navHostController::popBackStack,
            dispatch = pictureViewModel::dispatch,
            bookmarkDispatch = bookmarkViewModel::dispatch,
            followDispatch = followViewModel::dispatch,
            navToSearchResultScreen = navHostController::navigateToOutsideSearchResultScreen,
            popBackToHomeScreen = navHostController::popBackToMainScreen,
            navToUserDetailScreen = navHostController::navigateToOtherProfileDetailScreen,
        )
    } else {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun PictureScreen(
    state: PictureState,
    exception: Throwable?,
    bookmarkState: BookmarkState,
    followState: FollowState,
    illust: Illust,
    modifier: Modifier = Modifier,
    navToPictureScreen: (Illust, String) -> Unit = { _, _ -> },
    popBackStack: () -> Unit = {},
    dispatch: (PictureAction) -> Unit = {},
    bookmarkDispatch: (BookmarkAction) -> Unit = {},
    followDispatch: (FollowAction) -> Unit = {},
    navToSearchResultScreen: (String) -> Unit = {},
    popBackToHomeScreen: () -> Unit = {},
    navToUserDetailScreen: (Long) -> Unit = {},
) {
    val context = LocalPlatformContext.current
    val (relatedSpanCount, userSpanCount) = when (getOrientation()) {
        Orientation.Vertical -> Pair(2, 3)
        Orientation.Horizontal -> Pair(4, 6)
        else -> Pair(2, 3)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()
    val currPage =
        remember {
            derivedStateOf {
                minOf(
                    lazyStaggeredGridState.firstVisibleItemIndex,
                    illust.pageCount - 1
                )
            }
        }
    val isBarVisible by remember { derivedStateOf { lazyStaggeredGridState.firstVisibleItemIndex <= illust.pageCount } }
    var isScrollToBottom by rememberSaveable { mutableStateOf(false) }
    val isScrollToRelatedBottom = rememberSaveable { mutableStateOf(false) }

    val isFollowed = followState.followStatus[illust.user.id] ?: false
    val placeholder = rememberVectorPainter(Icons.Rounded.Refresh)
    var openBottomSheet by rememberSaveable { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState()
    var currLongClickPic by rememberSaveable { mutableStateOf(Pair(0, "")) }
    var currLongClickPicSize by rememberSaveable { mutableFloatStateOf(0f) }
    var loading by rememberSaveable { mutableStateOf(false) }
    val readMediaImagePermission =
        rememberPermissionState(permission = Permission.ReadStorage)
    val illustRepository = koinInject<IllustRepository>()
    val shareLauncher = rememberShareLauncher {
        loading = false
        scope.launch {
            snackbarHostState.showSnackbar(getString(Res.string.sharing_success))
        }
    }

    LaunchedEffect(currLongClickPic.second) {
        if (currLongClickPic.second.isNotEmpty()) {
            launchNetwork {
                currLongClickPicSize = calculateImageSize(currLongClickPic.second)
            }
        }
    }

    var currentLoadingItem by rememberSaveable { mutableIntStateOf(0) }
    LaunchedEffect(state.illustRelated.size) {
        currentLoadingItem = if (state.illustRelated.size.isEven()) {
            4
        } else {
            5
        }
    }
    LaunchedEffect(exception) {
        if (exception != null) {
            scope.launch {
                Logger.i("PictureScreen exception: ${exception.message}")
                snackbarHostState.showSnackbar(
                    exception.message ?: getString(Res.string.unknown_error)
                )
            }
        }
    }
//    with(sharedTransitionScope) {
    Screen(
//        modifier = modifier.sharedBounds(
//            rememberSharedContentState(key = "${prefix}-card-${illust.id}"),
//            animatedContentScope,
//            clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(10.dp))
//        ),
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 15.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.CenterStart),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.throttleClick { popBackStack() },
                            )
                            Icon(
                                imageVector = Icons.Rounded.Home,
                                contentDescription = null,
                                modifier = Modifier
                                    .padding(start = 15.dp)
                                    .throttleClick { popBackToHomeScreen() }
                            )
                        }
                        // 分享按钮
                        Icon(
                            imageVector = Icons.Rounded.Share,
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .throttleClick {
                                    share(
                                        "${illust.title} | ${illust.user.name} #pixiv https://www.pixiv.net/artworks/${illust.id}",
                                        shareLauncher
                                    )
                                },
                        )
                        this@TopAppBar.AnimatedVisibility(
                            modifier = Modifier.align(Alignment.Center),
                            visible = isBarVisible,
                            enter = fadeIn(),
                            exit = fadeOut(),
                        ) {
                            Text(
                                text = "${currPage.value + 1}/${illust.pageCount}",
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
                    .copy(containerColor = Color.Transparent)
            )
        },
        floatingActionButton = {
            Box(
                Modifier
//                    .sharedElement(
//                        rememberSharedContentState(key = "${prefix}-favorite-${illust.id}"),
//                        animatedContentScope,
//                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                    )
                    .throttleClick {
                        if (bookmarkState.bookmarkStatus[illust.id] == true) {
                            bookmarkDispatch(
                                BookmarkAction.IllustBookmarkDeleteIntent(
                                    illust.id
                                )
                            )
                        } else {
                            bookmarkDispatch(BookmarkAction.IllustBookmarkAddIntent(illust.id))
                        }
                    }
                    .shadow(5.dp, CircleShape)
                    .background(
                        if (!isSystemInDarkTheme()) Color.White else Color.DarkGray,
                    )
                    .padding(10.dp)

            ) {
                val isBookmarked =
                    bookmarkState.bookmarkStatus[illust.id] ?: illust.isBookmarked
                Icon(
                    imageVector = if (isBookmarked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                    contentDescription = null,
                    tint = if (isBookmarked) Color.Red else LocalContentColor.current,
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                )
            }
        },
        snackBarHost = {
            SnackbarHost(snackbarHostState) {
                TextSnackbar(
                    text = it.visuals.message,
                )
            }
        },
    ) {

        LazyVerticalStaggeredGrid(
            state = lazyStaggeredGridState,
            columns = StaggeredGridCells.Fixed(relatedSpanCount),
            modifier = Modifier
                .fillMaxSize()
        ) {
//            with(sharedTransitionScope) {
            if (illust.type == Type.Ugoira) {
                item(span = StaggeredGridItemSpan.FullLine, key = "ugoira") {
                    UgoiraPlayer(
                        images = state.ugoiraImages,
                        placeholder = placeholder
                    )
                }
            } else {
                items(
                    illust.pageCount,
                    key = { "${illust.id}_$it" },
                    span = { StaggeredGridItemSpan.FullLine }
                ) { index ->
                    val firstImageKey = "image-${illust.id}-0"
                    if (illust.pageCount > 1) {
                        illust.metaPages?.get(index)?.let {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(it.imageUrls?.large)
                                    .placeholderMemoryCacheKey("image-${illust.id}-$index")
                                    .build(),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
//                                        .then(
//                                            if (index == 0) Modifier.sharedElement(
//                                                sharedTransitionScope.rememberSharedContentState(
//                                                    key = "${prefix}-$firstImageKey"
//                                                ),
//                                                animatedVisibilityScope = animatedContentScope,
//                                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                            )
//                                            else Modifier
//                                        )
                                    .throttleClick(
                                        onLongClick = {
                                            currLongClickPic =
                                                Pair(index, it.imageUrls?.original ?: "")
                                            openBottomSheet = true
                                        }
                                    ),
                                contentScale = ContentScale.FillWidth,
                                placeholder = placeholder,
                            )
                        }
                    } else {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data(illust.imageUrls.large)
                                .placeholderMemoryCacheKey("image-${illust.id}-$index")
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
//                                    .then(
//                                        if (index == 0) Modifier.sharedElement(
//                                            sharedTransitionScope.rememberSharedContentState(key = "${prefix}-$firstImageKey"),
//                                            animatedVisibilityScope = animatedContentScope,
//                                            placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                        )
//                                        else Modifier
//                                    )
                                .throttleClick(
                                    onLongClick = {
                                        currLongClickPic =
                                            Pair(0, illust.metaSinglePage.originalImageURL)
                                        openBottomSheet = true
                                    }
                                ),
                            contentScale = ContentScale.FillWidth,
                            placeholder = placeholder,
                        )
                    }
                }
            }
//            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_title") {
                if (isScrollToBottom) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(vertical = 10.dp)
                    ) {
                        UserAvatar(
                            url = illust.user.profileImageUrls.medium,
                            onClick = {
                                navToUserDetailScreen(illust.user.id)
                            },
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .size(30.dp)
                                .align(Alignment.CenterVertically),
                        )
                        Column(
                            modifier = Modifier.padding(start = 10.dp)
                        ) {
                            Text(
                                text = illust.title,
//                                modifier = Modifier
//                                    .sharedElement(
//                                        rememberSharedContentState(key = "${prefix}-title-${illust.id}"),
//                                        animatedContentScope,
//                                        placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                    )
//                                    .skipToLookaheadSize(),
                                style = TextStyle(
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                maxLines = 1,
                            )
                            Text(
                                text = illust.user.name,
                                modifier = Modifier,
                                style = TextStyle(
                                    fontSize = 12.sp,
                                ),
                                maxLines = 1,
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }
            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_data") {
                Row(
                    Modifier.padding(top = 10.dp)
                ) {
                    Text(
                        text = convertUtcStringToLocalDateTime(illust.createDate),
                        modifier = Modifier.padding(start = 20.dp),
                        style = TextStyle(fontSize = 12.sp),
                    )
                    Text(
                        text = illust.totalView.toString() + " ${stringResource(Res.string.viewed)}",
                        Modifier.padding(start = 10.dp),
                        style = TextStyle(fontSize = 12.sp),
                    )
                    Text(
                        text = illust.totalBookmarks.toString() + " ${stringResource(Res.string.liked)}",
                        Modifier.padding(start = 10.dp),
                        style = TextStyle(fontSize = 12.sp),
                    )
                }
            }
            // tag
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_tags") {
                FlowRow(
                    Modifier.padding(start = 20.dp, top = 10.dp)
                ) {
                    illust.tags?.forEach {
                        Text(
                            text = "#" + it.name,
                            modifier = Modifier
                                .padding(vertical = 2.5.dp)
                                .padding(end = 5.dp)
                                .throttleClick {
                                    navToSearchResultScreen(it.name)
                                    dispatch(PictureAction.AddSearchHistory(it.name))
                                },
                            style = TextStyle(fontSize = 12.sp, color = deepBlue),
                        )
                        Text(
                            text = it.translatedName,
                            modifier = Modifier
                                .padding(vertical = 2.5.dp)
                                .padding(end = 10.dp),
                            style = TextStyle(fontSize = 12.sp)
                        )
                    }
                }
            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_divider_1") {
                HorizontalDivider(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(top = 50.dp)
                )
            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_author") {
                //作者头像、名字、关注按钮
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(top = 10.dp)
                ) {
                    UserAvatar(
                        url = illust.user.profileImageUrls.medium,
                        onClick = {
                            navToUserDetailScreen(illust.user.id)
                        },
                        modifier = Modifier
                            .size(30.dp)
                            .align(Alignment.CenterVertically),
                    )
                    Column(
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = illust.user.name,
//                            modifier = Modifier
//                                .sharedElement(
//                                    rememberSharedContentState(key = "${prefix}-user-name-${illust.user.id}"),
//                                    animatedContentScope,
//                                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                )
//                                .skipToLookaheadSize(),
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                        Text(
                            text = "ID: ${illust.user.id}",
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    if (isFollowed) {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .throttleClick {
                                    followDispatch(FollowAction.UnFollowUser(illust.user.id))
                                },
                            text = stringResource(Res.string.followed),
                            style = TextStyle(
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    } else {
                        Text(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .border(
                                    width = 1.dp,
                                    color = deepBlue,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .padding(horizontal = 10.dp, vertical = 8.dp)
                                .throttleClick {
                                    followDispatch(FollowAction.FollowUser(illust.user.id))
                                },
                            text = stringResource(Res.string.follow),
                            style = TextStyle(
                                color = deepBlue,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                }
            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_author_other_works") {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 15.dp)
                        .padding(top = 10.dp)
                ) {
//                    val otherPrefix = rememberSaveable { UUID.randomUUID().toString() }
//                    CompositionLocalProvider(
//                        LocalSharedKeyPrefix provides otherPrefix
//                    ) {
                    state.userIllusts.subList(0, minOf(userSpanCount, state.userIllusts.size))
                        .forEach {
                            SquareIllustItem(
                                illust = it,
                                bookmarkState = bookmarkState,
                                dispatch = bookmarkDispatch,
                                spanCount = minOf(userSpanCount, state.userIllusts.size),
                                horizontalPadding = 15.dp,
                                paddingValues = PaddingValues(2.dp),
                                elevation = 5.dp,
                                navToPictureScreen = navToPictureScreen
                            )
                        }
//                    }
                }
            }
            item(span = StaggeredGridItemSpan.FullLine, key = "illust_related_title") {
                //相关作品文字，显示在中间
                Text(
                    text = stringResource(Res.string.related_artworks),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 50.dp, bottom = 10.dp),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    ),
                )
            }
            itemsIndexed(
                state.illustRelated,
                key = { _, it -> "${illust.id}_related_${it.id}" },
                contentType = { _, _ -> "related_illusts" }
            ) { index, it ->
                // 相关作品
                SquareIllustItem(
                    illust = it,
                    bookmarkState = bookmarkState,
                    dispatch = bookmarkDispatch,
                    spanCount = relatedSpanCount,
                    paddingValues = PaddingValues(5.dp),
                    shouldShowTip = index == 0,
                    navToPictureScreen = navToPictureScreen
                )
            }

            items(currentLoadingItem, key = { "loading_$it" }) {
                val size =
                    (getScreenWidth() - 2 * relatedSpanCount * PaddingValues(
                        5.dp
                    ).calculateLeftPadding(
                        LayoutDirection.Ltr
                    ) - 1.dp) / relatedSpanCount
                Surface(
                    Modifier
                        .padding(5.dp)
                        .size(size),
                    shape = MaterialTheme.shapes.medium,
                    shadowElevation = 4.dp,
                    propagateMinConstraints = false,
                ) {

                }
            }

            item(key = "spacer") {
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
        lazyStaggeredGridState.OnScrollToBottom(
            illust.pageCount,
            illust.id
        ) {
            isScrollToBottom = it
        }
        lazyStaggeredGridState.OnScrollToBottom(loadingItemCount = currentLoadingItem) {
            isScrollToRelatedBottom.value = true
            state.nextUrl.queryParams.takeIf { it.isNotEmpty() }?.let {
                dispatch(PictureAction.LoadMoreIllustRelatedIntent(state.nextUrl.queryParams))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(
                visible = !isScrollToBottom,
                enter = fadeIn(),
                exit = fadeOut(),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(vertical = 10.dp)
                ) {
                    UserAvatar(
                        url = illust.user.profileImageUrls.medium,
                        onClick = {
                            navToUserDetailScreen(illust.user.id)
                        },
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .size(20.dp)
                            .align(Alignment.CenterVertically),
                    )
                    Column(
                        modifier = Modifier.padding(start = 10.dp)
                    ) {
                        Text(
                            text = illust.title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1,
                        )
                        Text(
                            text = illust.user.name,
                            style = TextStyle(
                                fontSize = 12.sp,
                            ),
                            maxLines = 1,
                        )
                    }
                }
            }
        }
        if (openBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    openBottomSheet = false
                },
                modifier = Modifier
                    .heightIn(getScreenHeight() / 2),
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .throttleClick {
                                loading = true
                                // 下载原始图片
                                dispatch(
                                    PictureAction.DownloadIllust(
                                        illust.id,
                                        currLongClickPic.first,
                                        currLongClickPic.second,
                                        context,
                                    ) {
                                        loading = false
                                    })
                                openBottomSheet = false
                            }
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.Download, contentDescription = null)
                        Text(
                            text = stringResource(
                                Res.string.download_with_size,
                                currLongClickPicSize.format(2)
                            ),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .throttleClick {
                                readMediaImagePermission.launchPermissionRequest()
                                if (readMediaImagePermission.status == PermissionStatus.Granted) {
                                    scope.launch(Dispatchers.IO) {
                                        loading = true
                                        if (createShareImage(
                                                currLongClickPic,
                                                illust,
                                                shareLauncher,
                                                illustRepository,
                                                context,
                                            )
                                        ) return@launch
                                        loading = false
                                        currLongClickPic = Pair(0, "")
                                    }
                                    openBottomSheet = false
                                }
                            }
                            .padding(vertical = 10.dp)
                    ) {
                        Icon(imageVector = Icons.Rounded.Share, contentDescription = null)
                        Text(
                            text = stringResource(Res.string.share),
                            modifier = Modifier.padding(start = 10.dp)
                        )
                    }
                }
            }
        }
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
//    }
}





@Composable
private fun LazyStaggeredGridState.OnScrollToBottom(
    pageCount: Int,
    id: Long,
    updateIsScrollToBottom: (Boolean) -> Unit,
) {
    val isToBottom by remember {
        derivedStateOf {
            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.find { it.key == "${id}_${pageCount - 1}" }
            if (lastVisibleItem != null) {
                return@derivedStateOf lastVisibleItem.index == pageCount - 1
                        && lastVisibleItem.let { it.offset.y + it.size.height } <= layoutInfo.let { it.viewportEndOffset - it.viewportStartOffset }
            } else {
                return@derivedStateOf false
            }
        }
    }
    LaunchedEffect(isToBottom) {
        updateIsScrollToBottom(isToBottom)
    }
}