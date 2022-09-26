package com.welu.composenavdestinations.navigation.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.DialogArgDestinationSpec

data class DialogArgDestinationScope<Arg: Any>(
    override val relatedSpec: DialogArgDestinationSpec<Arg>,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry
): ComposeArgDestinationScope<Arg> {

    override val args: Arg by lazy(LazyThreadSafetyMode.NONE) {
        relatedSpec.argsFrom(backStackEntry)
    }

}