package com.welu.composenavdestinations.result

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.welu.composenavdestinations.extensions.collectOnLifecycle
import com.welu.composenavdestinations.extensions.navigation.getBackStackEntry
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@PublishedApi
@Parcelize
internal class ResultWrapper<T>(
    val value: @RawValue T
) : Parcelable


@PublishedApi
internal inline fun <reified T> resultKey(
    forSpec: ComposeDestinationSpec<*>,
    keySpecification: String? = null
): String = forSpec.baseRoute + T::class.qualifiedName + keySpecification


inline fun <reified T> ComposeDestinationScope.sendDestinationResultTo(
    spec: ComposeDestinationSpec<*>,
    value: T?,
    keySpecification: String? = null
): Unit = navController.sendDestinationResultTo(
    spec = spec,
    value = value,
    keySpecification = keySpecification
)

inline fun <reified T> NavController.sendDestinationResultTo(
    spec: ComposeDestinationSpec<*>,
    value: T?,
    keySpecification: String? = null
) {
    getBackStackEntry(spec)?.let { entry ->
        entry.savedStateHandle[resultKey<T>(spec, keySpecification)] = ResultWrapper(value)
    } ?: throw IllegalStateException("Could not find the BackStackEntry for the specified NavDestinationSpec")
}

@Composable
inline fun <reified T> ComposeDestinationScope.DestinationResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    keySpecification: String? = null,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) = navController.DestinationResultListener(
    forSpec = forSpec,
    keySpecification = keySpecification,
    withLifecycleState = withLifecycleState,
    callback = callback
)

/**
 * This Result listener uses a flow and will be triggered when the lifecycle of the flow is active.
 * It is therefore possible for a Screen to send itself results or react to them as long as the screen is in the started State.
 */
@Composable
inline fun <reified T> NavController.DestinationResultListener(
    forSpec: ComposeDestinationSpec<*>,
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
inline fun <reified T> ComposeDestinationScope.LifecycleDestinationResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    keySpecification: String? = null,
    crossinline callback: (T) -> Unit
) = navController.LifecycleDestinationResultListener(
    forSpec = forSpec,
    keySpecification = keySpecification,
    callback = callback
)

/**
 * This Result listener will only be triggered when the user navigates back from another destination.
 * Only an Lifecycle Observer is needed and the key/value will be removed from the saveStateHandle once collected.
 */
@Composable
inline fun <reified T> NavController.LifecycleDestinationResultListener(
    forSpec: ComposeDestinationSpec<*>,
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