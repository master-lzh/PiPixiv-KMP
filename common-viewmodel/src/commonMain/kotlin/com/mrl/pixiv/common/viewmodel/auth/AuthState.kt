package com.mrl.pixiv.common.viewmodel.auth

import androidx.compose.runtime.Stable
import com.mrl.pixiv.common.viewmodel.State

@Stable
data class AuthState(
    val isLogin: Boolean,
) : State {
    companion object {
        val INITIAL = AuthState(isLogin = false)
    }
}

