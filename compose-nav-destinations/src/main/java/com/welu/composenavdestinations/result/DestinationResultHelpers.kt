package com.welu.composenavdestinations.result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
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

inline fun <reified T> ComposeDestinationScope.sendResultTo(
    destination: ComposeDestination<*>,
    result: DestinationResult<T>
): Unit = navController.sendResultTo(
    destination = destination,
    result = result
)

inline fun <reified T : DestinationResult<*>> ComposeDestinationScope.sendResultToPreviousDestination(result: T) {
    navController.sendResultToPreviousDestination(result)
}


inline fun <reified T : DestinationResult<*>> NavController.sendResultTo(
    spec: ComposeDestinationSpec<*>,
    result: T
) {
    getBackStackEntry(spec).sendResult(result)
}

inline fun <reified T> NavController.sendResultTo(
    destination: ComposeDestination<*>,
    result: DestinationResult<T>
) {
    getBackStackEntry(destination).sendResult(result)
}

inline fun <reified T : DestinationResult<*>> NavController.sendResultToPreviousDestination(result: T) {
    previousBackStackEntry?.sendResult(result = result)
}

inline fun <reified T : DestinationResult<*>> NavBackStackEntry.sendResult(result: T) {
    savedStateHandle[result.key] = result.value
}


//------------------------------------------------------------------------------------
// Lifecycle-ResultListeners
//------------------------------------------------------------------------------------

@Composable
inline fun <reified T> ComposeDestinationScope.LifecycleResultListener(
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) = navController.LifecycleResultListener(
    forSpec = forSpec,
    key = key,
    callback = callback
)

@Composable
inline fun <reified T> NavController.LifecycleResultListener(
    forDestination: ComposeDestination<*>,
    key: String = T::class.java.name,
    crossinline callback: (T) -> Unit
) {
    LifecycleResultListener(
        forNavBackStackEntry = getBackStackEntry(forDestination),
        key = key,
        callback = callback
    )
}

@Composable
inline fun <reified T> NavController.LifecycleResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) {
    LifecycleResultListener(
        forNavBackStackEntry = getBackStackEntry(forSpec),
        key = key,
        callback = callback
    )
}

/**
 * This Result listener will only be triggered when the user navigates back from another destination.
 * Only an Lifecycle Observer is needed and the key/value will be removed from the saveStateHandle once collected.
 */
@Composable
inline fun <reified T> NavController.LifecycleResultListener(
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.qualifiedName!!,
    crossinline callback: (T) -> Unit
) {
    DisposableEffect(Unit) {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                when (event) {
                    Lifecycle.Event.ON_RESUME -> {
                        if (!forNavBackStackEntry.savedStateHandle.contains(key)) return
                        forNavBackStackEntry.savedStateHandle.remove<T>(key)?.let(callback)
                    }
                    Lifecycle.Event.ON_DESTROY -> forNavBackStackEntry.lifecycle.removeObserver(this)
                    else -> Unit
                }
            }
        }
        forNavBackStackEntry.lifecycle.addObserver(observer)

        onDispose {
            //Alle aussser der hier waren davor: currentBackStackEntry
            forNavBackStackEntry.lifecycle.removeObserver(observer)
        }
    }
}


//------------------------------------------------------------------------------------
// Flow-ResultListeners
//------------------------------------------------------------------------------------

@Composable
inline fun <reified T> ComposeDestinationScope.ResultListener(
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
inline fun <reified T> NavController.ResultListener(
    forDestination: ComposeDestination<*>,
    key: String = T::class.java.name,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    ResultListener(
        forNavBackStackEntry = getBackStackEntry(forDestination),
        key = key,
        withLifecycleState = withLifecycleState,
        callback = callback
    )
}

@Composable
inline fun <reified T> NavController.ResultListener(
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!,
    withLifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    crossinline callback: (T) -> Unit
) {
    ResultListener(
        forNavBackStackEntry = getBackStackEntry(forSpec),
        key = key,
        withLifecycleState = withLifecycleState,
        callback = callback
    )
}

/**
 * This Result listener uses a flow and will be triggered when the lifecycle of the flow is active.
 * It is therefore possible for a Screen to send itself results or react to them as long as the screen is in the started State.
 */
@Composable
inline fun <reified T> NavController.ResultListener(
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



//------------------------------------------------------------------------------------
// LifecycleResult State-Collection functions
//------------------------------------------------------------------------------------

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResultWithLifecycle(
    initialValue: T,
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResultWithLifecycle(
    initialValue = initialValue,
    forSpec = forSpec,
    key = key
)

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResultWithLifecycle(
    initialValue: T,
    forDestination: ComposeDestination<*>,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResultWithLifecycle(
    initialValue = initialValue,
    forDestination = forDestination,
    key = key
)

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResultWithLifecycle(
    initialValue: T,
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResultWithLifecycle(
    initialValue = initialValue,
    forNavBackStackEntry = forNavBackStackEntry,
    key = key
)

@Composable
inline fun <reified T> NavController.receiveResultWithLifecycle(
    initialValue: T,
    forDestination: ComposeDestination<*>,
    key: String = T::class.qualifiedName!!
): State<T> = receiveResultWithLifecycle(
    initialValue = initialValue,
    forNavBackStackEntry = getBackStackEntry(forDestination),
    key = key
)
@Composable
inline fun <reified T> NavController.receiveResultWithLifecycle(
    initialValue: T,
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!
): State<T> = receiveResultWithLifecycle(
    initialValue = initialValue,
    forNavBackStackEntry = getBackStackEntry(forSpec),
    key = key
)

@Composable
inline fun <reified T> NavController.receiveResultWithLifecycle(
    initialValue: T,
    forNavBackStackEntry: NavBackStackEntry = currentBackStackEntry!!,
    key: String = T::class.qualifiedName!!
): State<T> {
    val state = remember {
        mutableStateOf(initialValue)
    }

    LifecycleResultListener(
        forNavBackStackEntry = forNavBackStackEntry,
        key = key,
        callback = state::value::set
    )

    return state
}



//------------------------------------------------------------------------------------
// FlowResult State-Collection functions
//------------------------------------------------------------------------------------

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResult(
    initialValue: T,
    forSpec: ComposeDestinationSpec<*> = relatedSpec,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResult(
    initialValue = initialValue,
    forSpec = forSpec,
    key = key
)

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResult(
    initialValue: T,
    forDestination: ComposeDestination<*>,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResult(
    initialValue = initialValue,
    forDestination = forDestination,
    key = key
)

@Composable
inline fun <reified T> ComposeDestinationScope.receiveResult(
    initialValue: T,
    forNavBackStackEntry: NavBackStackEntry,
    key: String = T::class.qualifiedName!!
): State<T> = navController.receiveResult(
    initialValue = initialValue,
    forNavBackStackEntry = forNavBackStackEntry,
    key = key
)


@Composable
inline fun <reified T> NavController.receiveResult(
    initialValue: T,
    forSpec: ComposeDestinationSpec<*>,
    key: String = T::class.qualifiedName!!
): State<T> = receiveResult(
    initialValue = initialValue,
    forNavBackStackEntry = getBackStackEntry(forSpec),
    key = key
)

@Composable
inline fun <reified T> NavController.receiveResult(
    initialValue: T,
    forDestination: ComposeDestination<*>,
    key: String = T::class.qualifiedName!!
): State<T> = receiveResult(
    initialValue = initialValue,
    forNavBackStackEntry = getBackStackEntry(forDestination),
    key = key
)

@Composable
inline fun <reified T> NavController.receiveResult(
    initialValue: T,
    forNavBackStackEntry: NavBackStackEntry = currentBackStackEntry!!,
    key: String = T::class.qualifiedName!!
): State<T> {
    val state = remember {
        mutableStateOf(initialValue)
    }

    ResultListener(
        forNavBackStackEntry = forNavBackStackEntry,
        key = key,
        callback = state::value::set
    )

    return state
}


@Composable
inline fun <reified T> NavController.receiveSaveableResult(
    initialValue: T,
    forNavBackStackEntry: NavBackStackEntry = currentBackStackEntry!!,
    key: String = T::class.qualifiedName!!,
    saver: Saver<MutableState<T>, out Any>? = null
): State<T> {
    val state = if(saver != null) {
        rememberSaveable(saver = saver) {
            mutableStateOf(initialValue)
        }
    } else {
        rememberSaveable {
            mutableStateOf(initialValue)
        }
    }

    ResultListener(
        forNavBackStackEntry = forNavBackStackEntry,
        key = key,
        callback = state::value::set
    )

    return state
}