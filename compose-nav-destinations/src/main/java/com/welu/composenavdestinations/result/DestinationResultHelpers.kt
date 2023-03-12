package com.welu.composenavdestinations.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.extensions.collectOnLifecycle
import com.welu.composenavdestinations.extensions.navigation.getBackStackEntry
import com.welu.composenavdestinations.navigation.destinations.ComposeDestination
import com.welu.composenavdestinations.navigation.scope.ComposeDestinationScope
import com.welu.composenavdestinations.navigation.spec.ComposeDestinationSpec

inline fun <reified T : DestinationResult<*>> ComposeDestinationScope.sendResultTo(
    spec: ComposeDestinationSpec<*>,
    result: T
): Unit = navController.sendResultTo(
    spec = spec,
    result = result
)

inline fun <reified T : DestinationResult<*>> ComposeDestinationScope.sendResultTo(
    navBackStackEntry: NavBackStackEntry,
    result: T
): Unit = navController.sendResultTo(
    navBackStackEntry = navBackStackEntry,
    result = result
)

inline fun <reified T : DestinationResult<*>> NavController.sendResultTo(
    spec: ComposeDestinationSpec<*>,
    result: T
) {
    getBackStackEntry(spec)?.let { entry ->
        sendResultTo(entry, result)
    } ?: throw IllegalStateException("Could not find the BackStackEntry for the specified ComposeDestinationSpec")
}

inline fun <reified T : DestinationResult<*>> NavController.sendResultTo(
    navBackStackEntry: NavBackStackEntry,
    result: T
) {
    navBackStackEntry.savedStateHandle[result.key] = result.value
}

inline fun <reified T : Any> NavController.sendResultTo(
    destination: ComposeDestination<*>,
    result: DestinationResult<T>
) {
    getBackStackEntry(destination)?.let { entry ->
        sendResultTo(entry, result)
    } ?: throw IllegalStateException("Could not find the BackStackEntry for the specified ComposeDestinationSpec")
}

inline fun <reified T : Any> ComposeDestinationScope.sendResultTo(
    destination: ComposeDestination<*>,
    result: DestinationResult<T>
): Unit = navController.sendResultTo(
    destination = destination,
    result = result
)


inline fun <reified T : DestinationResult<*>> NavController.sendResultToPreviousDestination(result: T) {
    previousBackStackEntry?.let { destination ->
        sendResultTo(
            navBackStackEntry = destination,
            result = result
        )
    }
}

inline fun <reified T : DestinationResult<*>> ComposeDestinationScope.sendResultToPreviousDestination(result: T) {
    navController.sendResultToPreviousDestination(result)
}


@Composable
inline fun <reified T> NavController.LifecycleResultListener(
    forDestination: ComposeDestination<*>,
    key: String = T::class.java.name,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forDestination)?.let { backStackEntry ->
        LifecycleResultListener(
            forNavBackStackEntry = backStackEntry,
            key = key,
            callback = callback
        )
    }
}

@Composable
inline fun <reified T : Any> ComposeDestinationScope.LifecycleResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) = navController.LifecycleResultListener(
    forSpec = forSpec,
    key = key,
    callback = callback
)

@Composable
inline fun <reified T : Any> NavController.LifecycleResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forSpec)?.let { backStackEntry ->
        LifecycleResultListener(
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
inline fun <reified T : Any> NavController.LifecycleResultListener(
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) {
    DisposableEffect(Unit) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if (currentBackStackEntry?.savedStateHandle?.contains(key) != true) return
                        currentBackStackEntry?.savedStateHandle?.remove<T>(key)?.let(callback)
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


@Composable
inline fun <reified T> NavController.ResultListener(
    forDestination: ComposeDestination<*>,
    key: String = T::class.java.name,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forDestination)?.let { backStackEntry ->
        ResultListener(
            forNavBackStackEntry = backStackEntry,
            key = key,
            withLifecycleState = withLifecycleState,
            callback = callback
        )
    }
}

@Composable
inline fun <reified T : Any> ComposeDestinationScope.ResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.qualifiedName!!,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) = navController.ResultListener(
    forSpec = forSpec,
    key = key,
    withLifecycleState = withLifecycleState,
    callback = callback
)

@Composable
inline fun <reified T : Any> NavController.ResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    getBackStackEntry(forSpec)?.let { backStackEntry ->
        ResultListener(
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
inline fun <reified T : Any> NavController.ResultListener(
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.qualifiedName!!,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<T?>(key, null)
        ?.collectOnLifecycle(minState = withLifecycleState) { value ->
            if (value == null) return@collectOnLifecycle
            callback(value)
            forNavBackStackEntry.savedStateHandle.set<T>(key, null)
        }
}