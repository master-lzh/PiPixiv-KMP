package com.mrl.pixiv.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.mrl.pixiv.common.ui.LocalNavigator
import com.mrl.pixiv.common.ui.Screen
import com.mrl.pixiv.common.ui.components.UserAvatar
import com.mrl.pixiv.common.ui.currentOrThrow
import com.mrl.pixiv.common_ui.item.SettingItem
import com.mrl.pixiv.common_ui.util.navigateToHistoryScreen
import com.mrl.pixiv.common_ui.util.navigateToSelfCollectionScreen
import com.mrl.pixiv.common_ui.util.navigateToSelfProfileDetailScreen
import com.mrl.pixiv.common_ui.util.navigateToSettingScreen
import com.mrl.pixiv.data.setting.SettingTheme
import com.mrl.pixiv.data.setting.getAppCompatDelegateThemeMode
import com.mrl.pixiv.profile.viewmodel.ProfileAction
import com.mrl.pixiv.profile.viewmodel.ProfileState
import com.mrl.pixiv.profile.viewmodel.ProfileViewModel
import com.mrl.pixiv.util.throttleClick
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import pipixiv.feature.profile.generated.resources.Res
import pipixiv.feature.profile.generated.resources.collection
import pipixiv.feature.profile.generated.resources.history
import pipixiv.feature.profile.generated.resources.preference

expect val options: Map<SettingTheme, StringResource>

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = koinViewModel(),
    navHostController: NavHostController = LocalNavigator.currentOrThrow,
) {
    ProfileScreen_(
        modifier = modifier,
        state = viewModel.state,
        dispatch = viewModel::dispatch,
        navToProfileDetail = navHostController::navigateToSelfProfileDetailScreen,
        navToSetting = navHostController::navigateToSettingScreen,
        navToHistory = navHostController::navigateToHistoryScreen,
        navToCollection = navHostController::navigateToSelfCollectionScreen,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
internal fun ProfileScreen_(
    modifier: Modifier = Modifier,
    state: ProfileState = ProfileState.INITIAL,
    dispatch: (ProfileAction) -> Unit = {},
    navToProfileDetail: () -> Unit = {},
    navToSetting: () -> Unit = {},
    navToHistory: () -> Unit = {},
    navToCollection: () -> Unit = {},
) {
    Screen(
        topBar = {
            var expanded by remember { mutableStateOf(false) }
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { expanded = true }) {
                        Icon(imageVector = Icons.Rounded.Palette, contentDescription = null)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        options.forEach { (theme, resId) ->
                            DropdownMenuItem(
                                text = {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = stringResource(resId),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        if (getAppCompatDelegateThemeMode() == theme) {
                                            Icon(
                                                imageVector = Icons.Rounded.Check,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                },
                                onClick = {
                                    dispatch(ProfileAction.ChangeAppTheme(theme = theme))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it)
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // 头像和昵称
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
//                    with(LocalSharedTransitionScope.currentOrThrow) {
                    UserAvatar(
                        url = state.user.avatar,
                        modifier = Modifier
                            .size(80.dp),
//                            .sharedElement(
//                                state = rememberSharedContentState(key = "user-avatar-${state.user.uid}"),
//                                LocalAnimatedContentScope.currentOrThrow,
//                                placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                            ),
                        onClick = navToProfileDetail
                    )
                    Column {
                        // 昵称
                        Text(
                            text = state.user.username,
//                            modifier = Modifier
//                                .sharedElement(
//                                    rememberSharedContentState(key = "user-name-${state.user.uid}"),
//                                    LocalAnimatedContentScope.currentOrThrow,
//                                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                )
//                                .skipToLookaheadSize()
                        )
                        // ID
                        Text(
                            text = "ID: ${state.user.uid}",
//                            modifier = Modifier
//                                .sharedElement(
//                                    rememberSharedContentState(key = "user-id-${state.user.uid}"),
//                                    LocalAnimatedContentScope.currentOrThrow,
//                                    placeHolderSize = SharedTransitionScope.PlaceHolderSize.animatedSize
//                                )
//                                .skipToLookaheadSize()
                        )
                    }
//                    }
                }
            }
            item {
                HorizontalDivider()
            }
            item {
                Column {
                    // 偏好设置
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .throttleClick(
                                indication = rememberRipple()
                            ) {
                                navToSetting()
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .padding(vertical = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(imageVector = Icons.Rounded.Settings, contentDescription = null)
                            Text(
                                text = stringResource(Res.string.preference),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    // 历史记录
                    SettingItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.History,
                                contentDescription = null
                            )
                        },
                        onClick = navToHistory
                    ) {
                        Text(
                            text = stringResource(Res.string.history),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }

                    // 收藏
                    SettingItem(
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Bookmarks,
                                contentDescription = null
                            )
                        },
                        onClick = navToCollection
                    ) {
                        Text(
                            text = stringResource(Res.string.collection),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}