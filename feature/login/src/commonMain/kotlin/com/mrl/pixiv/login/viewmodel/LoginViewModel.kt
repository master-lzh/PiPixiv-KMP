package com.mrl.pixiv.login.viewmodel

import com.mrl.pixiv.common.viewmodel.BaseViewModel
import com.mrl.pixiv.common.viewmodel.auth.AuthAction
import com.mrl.pixiv.common.viewmodel.auth.AuthMiddleware
import com.mrl.pixiv.common.viewmodel.auth.AuthReducer
import com.mrl.pixiv.common.viewmodel.auth.AuthState

class LoginViewModel(
    reducer: AuthReducer,
    authMiddleware: AuthMiddleware,
) : BaseViewModel<AuthState, AuthAction>(
    initialState = AuthState.INITIAL,
    reducer = reducer,
    middlewares = listOf(authMiddleware)
) {

}