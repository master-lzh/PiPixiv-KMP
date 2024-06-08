package com.mrl.pixiv.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import co.touchlab.kermit.Logger as Log


private const val BufferSize = 64

abstract class BaseViewModel<S : State, A : Action>(
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<S, A>> = emptyList(),
    initialState: S,
) : ViewModel(), Dispatcher<A> {
    private val _exception = MutableSharedFlow<Throwable?>()
    val exception = _exception.asSharedFlow()

    private data class ActionImpl<S : State, A : Action>(
        val state: S,
        val action: A,
    )

    private val actions = MutableSharedFlow<ActionImpl<S, A>>(extraBufferCapacity = BufferSize)

    var state: S by mutableStateOf(initialState)
        private set

    init {
        middlewares.forEach { middleware ->
            middleware.setDispatcher(this)
            middleware.setScope(viewModelScope)
        }
        viewModelScope.launch {
            actions.onEach {
                Log.d("actions: $it", tag = this@BaseViewModel::class.simpleName!!)
                middlewares.forEach { middleware ->
                    middleware.process(it.state, it.action)
                }
            }.collect()
        }
        viewModelScope.launch {
            actions.collect {
                state = reducer.reduce(state, it.action)
            }
        }
    }

    override fun dispatch(action: A) {
        val success = try {
            actions.tryEmit(ActionImpl(state, action))
        } catch (e: Exception) {
            Log.e("dispatch error", e, this@BaseViewModel::class.simpleName!!)
            false
        }
        if (!success) error("MVI action buffer overflow")
    }

    override fun dispatchError(throwable: Throwable?) {
        viewModelScope.launch {
            _exception.emit(throwable)
        }
    }

    open fun onCreate() {

    }

    open fun onStart() {

    }

    override fun onCleared() {
        super.onCleared()
        middlewares.forEach { middleware ->
            middleware.onClear()
        }
    }
}