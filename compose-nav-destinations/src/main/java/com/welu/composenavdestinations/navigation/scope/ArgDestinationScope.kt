package com.welu.composenavdestinations.navigation.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.welu.composenavdestinations.navigation.spec.ArgDestinationSpec

data class ArgDestinationScope<Arg : Any>(
    override val relatedSpec: ArgDestinationSpec<Arg>,
    override val navController: NavController,
    override val backStackEntry: NavBackStackEntry,
    private val lazyArgs: Lazy<Arg>
) : DestinationScope {

    val args: Arg by lazyArgs

}
