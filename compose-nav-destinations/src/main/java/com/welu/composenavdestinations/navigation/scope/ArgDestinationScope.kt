package com.welu.composenavdestinations.navigation.scope

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.ArgDestinationSpec

data class ArgDestinationScope<Arg : Any>(
    override val relatedSpec: ArgDestinationSpec<Arg>,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry,
    val animatedVisibilityScope: AnimatedVisibilityScope
) : ComposeArgDestinationScope<Arg> {

    override val args: Arg by lazy(LazyThreadSafetyMode.NONE) {
        relatedSpec.argsFrom(backStackEntry)
    }

}