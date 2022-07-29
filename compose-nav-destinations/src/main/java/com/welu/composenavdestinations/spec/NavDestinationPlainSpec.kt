package com.welu.composenavdestinations.spec

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavBackStackEntry

interface NavDestinationPlainSpec: NavDestinationSpec<Unit> {

    operator fun invoke(): Routable = Routable(route)

    override val baseRoute: String get() = route

    override fun getArgs(navBackStackEntry: NavBackStackEntry) = Unit

    override fun getArgs(savedStateHandle: SavedStateHandle) = Unit

}