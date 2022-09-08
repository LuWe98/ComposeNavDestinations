package com.welu.composenavdestinations.result

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.*
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.spec.NavDestinationSpec
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@PublishedApi
@Parcelize
internal class ResultWrapper<T>(
    val value: @RawValue T
) : Parcelable

inline fun <reified T> NavController.sendResult(toSpec: NavDestinationSpec, value: T?, keySpecification: String? = null) {
    val previousBackStackEntry = findBackStackEntryWith(toSpec)
        ?: throw IllegalStateException("Could not find the BackStackEntry for the specified NavDestinationSpec")
    previousBackStackEntry.savedStateHandle[resultKey<T>(toSpec, keySpecification)] = ResultWrapper(value)
}

/**
 * This Result listener uses a flow and will be triggered when the lifecycle of the flow is active.
 * It is therefore possible for a Screen to send itself results or react to them as long as the screen is in the started State.
 */
@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.flowResultListener(forSpec: NavDestinationSpec, keySpecification: String? = null, crossinline callback: (T) -> Unit) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<ResultWrapper<T?>?>(resultKey<T>(forSpec, keySpecification), null)
        ?.collectOnLifecycle { wrapper ->
            wrapper?.let {
                if (wrapper.value is T) {
                    callback(wrapper.value)
                    findBackStackEntryWith(forSpec)?.savedStateHandle?.set<T>(resultKey<T>(forSpec, keySpecification), null)
                } else {
                    throw IllegalArgumentException("ResultWrapper value ${wrapper.value}, is not assignable!")
                }
            }
        }
}

/**
 * This Result listener will only be triggered when the user navigates back from another destination.
 * Only an Lifecycle Observer is needed and the key/value will be removed from the saveStateHandle once collected.
 */
@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.lifecycleResultListener(forSpec: NavDestinationSpec, keySpecification: String? = null, crossinline callback: (T) -> Unit) {
    DisposableEffect(Unit) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        val key = resultKey<T>(forSpec, keySpecification)
                        if (currentBackStackEntry?.savedStateHandle?.contains(key) != true) return
                        currentBackStackEntry?.savedStateHandle?.remove<ResultWrapper<T?>>(key)?.let { wrapper ->
                            if (wrapper.value is T) {
                                callback(wrapper.value)
                            } else {
                                throw IllegalArgumentException("ResultWrapper value: ${wrapper.value}, is not assignable!")
                            }
                        }
                    }
                    Lifecycle.Event.ON_DESTROY -> currentBackStackEntry?.lifecycle?.removeObserver(this)
                    else -> Unit
                }
            }
        }
        currentBackStackEntry?.lifecycle?.addObserver(observer)
        onDispose {
            findBackStackEntryWith(forSpec)?.lifecycle?.removeObserver(observer)
        }
    }
}

@PublishedApi
internal fun NavController.findBackStackEntryWith(spec: NavDestinationSpec): NavBackStackEntry? {
    for (i in backQueue.lastIndex downTo 0) {
        if (backQueue[i].destination.route == spec.route) {
            return backQueue[i]
        }
    }
    return null
}

@PublishedApi
internal inline fun <reified T> resultKey(forSpec: NavDestinationSpec, keySpecification: String? = null): String =
    forSpec.baseRoute + T::class.qualifiedName + keySpecification

@PublishedApi
@SuppressLint("ComposableNaming")
@Composable
internal inline fun <reified T> Flow<T>.collectOnLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    minState: Lifecycle.State = Lifecycle.State.STARTED,
    noinline action: suspend (T) -> Unit
) {
    DisposableEffect(Unit) {
        lifecycleOwner.lifecycleScope.launch {
            flowWithLifecycle(lifecycleOwner.lifecycle, minState).collect(action)
        }.let { job ->
            onDispose(job::cancel)
        }
    }
}

//@Parcelize
//data class ResultWrapperTest(
//    val key: String
//): Parcelable

