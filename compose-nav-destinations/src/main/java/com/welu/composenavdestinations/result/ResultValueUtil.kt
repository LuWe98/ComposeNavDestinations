package com.welu.composenavdestinations.result

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
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

/*
@PublishedApi
internal inline fun <reified T> resultKey(
    forSpec: ComposeDestinationSpec<*>,
    keySpecification: String? = null
): String = forSpec.baseRoute + T::class.qualifiedName + keySpecification
 */

@PublishedApi
internal fun resultKey(
    forRoute: String,
    key: String
): String = forRoute + "_" + key

@PublishedApi
internal fun resultKey(
    forNavBackStackEntry: NavBackStackEntry,
    key: String
): String = resultKey(forNavBackStackEntry.destination.route!!, key)


inline fun <reified T> ComposeDestinationScope.sendDestinationResultTo(
    spec: ComposeDestinationSpec<*>,
    value: T,
    key: String = value!!::class.java.name
): Unit = navController.sendDestinationResultTo(
    spec = spec,
    value = value,
    key = key
)

inline fun <reified T> ComposeDestinationScope.sendDestinationResultTo(
    navBackStackEntry: NavBackStackEntry,
    value: T,
    key: String = value!!::class.java.name
): Unit = navController.sendDestinationResultTo(
    navBackStackEntry = navBackStackEntry,
    value = value,
    key = key
)

inline fun <reified T> NavController.sendDestinationResultTo(
    spec: ComposeDestinationSpec<*>,
    value: T,
    key: String = value!!::class.java.name
) {
    getBackStackEntry(spec)?.let { entry ->
        sendDestinationResultTo(entry, value, key)
    } ?: throw IllegalStateException("Could not find the BackStackEntry for the specified ComposeDestinationSpec")
}

inline fun <reified T> NavController.sendDestinationResultTo(
    navBackStackEntry: NavBackStackEntry,
    value: T,
    key: String = value!!::class.java.name
) {
    navBackStackEntry.savedStateHandle[resultKey(navBackStackEntry, key)] = ResultWrapper(value)
}

inline fun <reified T> NavController.sendResultToPreviousDestination(
    value: T,
    key: String = value!!::class.java.name
) {
    previousBackStackEntry?.let { destination ->
        sendDestinationResultTo(
            navBackStackEntry = destination,
            value = value,
            key = key
        )
    }
}

inline fun <reified T> ComposeDestinationScope.sendResultToPreviousDestination(
    value: T,
    key: String = value!!::class.java.name
) {
    navController.sendResultToPreviousDestination(
        value = value,
        key = key
    )
}

@Composable
inline fun <reified T> ComposeDestinationScope.DestinationResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.java.name,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) = navController.DestinationResultListener(
    forSpec = forSpec,
    key = key,
    withLifecycleState = withLifecycleState,
    callback = callback
)

@Composable
inline fun <reified T> NavController.DestinationResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.java.name,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forSpec)?.let { backStackEntry ->
        DestinationResultListener(
            forNavBackStackEntry = backStackEntry,
            key = key,
            withLifecycleState = withLifecycleState,
            callback = callback
        )
    }
}

/**
 * This Result listener uses a flow and will be triggered when the lifecycle of the flow is active.
 * It is therefore possible for a Screen to send itself results or react to them as long as the screen is in the started State.
 */
@Composable
inline fun <reified T> NavController.DestinationResultListener(
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.java.name,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<ResultWrapper<T?>?>(resultKey(forNavBackStackEntry, key), null)
        ?.collectOnLifecycle(minState = withLifecycleState) { wrapper ->
            if (wrapper == null) return@collectOnLifecycle
            if (wrapper.value !is T) throw IllegalArgumentException("ResultWrapper value ${wrapper.value}, is not assignable!")

            callback(wrapper.value)
            forNavBackStackEntry.savedStateHandle.set<T>(resultKey(forNavBackStackEntry, key), null)
        }
}

@Composable
inline fun <reified T> ComposeDestinationScope.LifecycleDestinationResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.java.name,
    crossinline callback: (T) -> Unit
) = navController.LifecycleDestinationResultListener(
    forSpec = forSpec,
    key = key,
    callback = callback
)

@Composable
inline fun <reified T> NavController.LifecycleDestinationResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.java.name,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forSpec)?.let { backStackEntry ->
        LifecycleDestinationResultListener(
            forNavBackStackEntry = backStackEntry,
            key = key,
            callback = callback
        )
    }
}

/**
 * This Result listener will only be triggered when the user navigates back from another destination.
 * Only an Lifecycle Observer is needed and the key/value will be removed from the saveStateHandle once collected.
 */
@Composable
inline fun <reified T> NavController.LifecycleDestinationResultListener(
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.java.name,
    crossinline callback: (T) -> Unit
) {
    DisposableEffect(Unit) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        val finalKey = resultKey(forNavBackStackEntry, key)
                        if (currentBackStackEntry?.savedStateHandle?.contains(finalKey) != true) return
                        currentBackStackEntry?.savedStateHandle?.remove<ResultWrapper<T?>>(finalKey)?.let { wrapper ->
                            if (wrapper.value !is T) throw IllegalArgumentException("ResultWrapper value: ${wrapper.value}, is not assignable!")
                            callback(wrapper.value)
                        }
                    }

                    Lifecycle.Event.ON_DESTROY -> currentBackStackEntry?.lifecycle?.removeObserver(this)
                    else -> Unit
                }
            }
        }
        currentBackStackEntry?.lifecycle?.addObserver(observer)
        onDispose {
            forNavBackStackEntry.lifecycle.removeObserver(observer)
        }
    }
}