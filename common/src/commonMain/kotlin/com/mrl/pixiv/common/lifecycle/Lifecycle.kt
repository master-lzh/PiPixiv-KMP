package com.mrl.pixiv.common.lifecycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import co.touchlab.kermit.Logger as Log

@Composable
fun OnLifecycle(
    lifecycleEvent: Lifecycle.Event = Lifecycle.Event.ON_CREATE,
    onLifecycle: () -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val onLifecycleCallback by rememberUpdatedState(newValue = onLifecycle)
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == lifecycleEvent) {
                onLifecycleCallback()
            }
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    Log.d("$lifecycleOwner onCreate")
                }

                Lifecycle.Event.ON_START -> {
                    Log.d("$lifecycleOwner On Start")
                }

                Lifecycle.Event.ON_RESUME -> {
                    Log.d("$lifecycleOwner On Resume")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("$lifecycleOwner On Pause")
                }

                Lifecycle.Event.ON_STOP -> {
                    Log.d("$lifecycleOwner On Stop")
                }

                Lifecycle.Event.ON_DESTROY -> {
                    Log.d("$lifecycleOwner On Destroy")
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun <T> Flow<T>.collectAsStateWithLifecycle(
    initialValue: T,
    lifecycle: Lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    return produceState(initialValue, this, lifecycle, minActiveState, context) {
        lifecycle.repeatOnLifecycle(minActiveState) {
            if (context == EmptyCoroutineContext) {
                this@collectAsStateWithLifecycle.collect { this@produceState.value = it }
            } else withContext(context) {
                this@collectAsStateWithLifecycle.collect { this@produceState.value = it }
            }
        }
    }
}

@Composable
@Suppress("StateFlowValueCalledInComposition") // Initial value for an ongoing collect.
fun <T> StateFlow<T>.collectAsStateWithLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> = collectAsStateWithLifecycle(
    initialValue = this.value,
    lifecycle = lifecycleOwner.lifecycle,
    minActiveState = minActiveState,
    context = context
)