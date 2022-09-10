package com.welu.composenavdestinations.result

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.extensions.collectOnLifecycle
import com.welu.composenavdestinations.spec.NavDestinationScope
import com.welu.composenavdestinations.spec.NavDestinationSpec
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@PublishedApi
@Parcelize
internal class ResultWrapper<T>(
    val value: @RawValue T
) : Parcelable


inline fun <reified T> NavController.sendDestinationResult(
    toScope: NavDestinationScope,
    value: T?,
    keySpecification: String? = null
) = sendDestinationResult(
    toSpec = toScope.spec,
    value = value,
    keySpecification = keySpecification
)

inline fun <reified T> NavController.sendDestinationResult(
    toSpec: NavDestinationSpec,
    value: T?,
    keySpecification: String? = null
) {
    getBackStackEntry(toSpec)?.let { entry ->
        entry.savedStateHandle[resultKey<T>(toSpec, keySpecification)] = ResultWrapper(value)
    } ?: throw IllegalStateException("Could not find the BackStackEntry for the specified NavDestinationSpec")
}


@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.destinationResultListener(
    forScope: NavDestinationScope,
    keySpecification: String? = null,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) = destinationResultListener(
    forSpec = forScope.spec,
    keySpecification = keySpecification,
    withLifecycleState = withLifecycleState,
    callback = callback
)

/**
 * This Result listener uses a flow and will be triggered when the lifecycle of the flow is active.
 * It is therefore possible for a Screen to send itself results or react to them as long as the screen is in the started State.
 */
@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.destinationResultListener(
    forSpec: NavDestinationSpec,
    keySpecification: String? = null,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<ResultWrapper<T?>?>(resultKey<T>(forSpec, keySpecification), null)
        ?.collectOnLifecycle(minState = withLifecycleState) { wrapper ->
            wrapper?.let {
                if (wrapper.value is T) {
                    callback(wrapper.value)
                    getBackStackEntry(forSpec)?.savedStateHandle?.set<T>(resultKey<T>(forSpec, keySpecification), null)
                } else {
                    throw IllegalArgumentException("ResultWrapper value ${wrapper.value}, is not assignable!")
                }
            }
        }
}

@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.lifecycleDestinationResultListener(
    forScope: NavDestinationScope,
    keySpecification: String? = null,
    crossinline callback: (T) -> Unit
) = lifecycleDestinationResultListener(
    forSpec = forScope.spec,
    keySpecification = keySpecification,
    callback = callback
)

/**
 * This Result listener will only be triggered when the user navigates back from another destination.
 * Only an Lifecycle Observer is needed and the key/value will be removed from the saveStateHandle once collected.
 */
@Composable
@SuppressLint("ComposableNaming")
inline fun <reified T> NavController.lifecycleDestinationResultListener(
    forSpec: NavDestinationSpec,
    keySpecification: String? = null,
    crossinline callback: (T) -> Unit
) {
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
            getBackStackEntry(forSpec)?.lifecycle?.removeObserver(observer)
        }
    }
}

@PublishedApi
internal fun NavController.getBackStackEntry(spec: NavDestinationSpec): NavBackStackEntry? {
    runCatching {
        getBackStackEntry(spec.route)
    }.onSuccess {
        return it
    }

//    for (i in backQueue.lastIndex downTo 0) {
//        if (backQueue[i].destination.route == spec.route) {
//            return backQueue[i]
//        }
//    }

    return null
}

@PublishedApi
internal inline fun <reified T> resultKey(
    forSpec: NavDestinationSpec,
    keySpecification: String? = null
): String = forSpec.baseRoute + T::class.qualifiedName + keySpecification


//@Parcelize
//data class ResultWrapperTest(
//    val key: String
//): Parcelable

