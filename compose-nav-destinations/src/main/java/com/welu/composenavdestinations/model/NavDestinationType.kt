package com.welu.composenavdestinations.model

sealed class NavDestinationType {
    object Default: NavDestinationType()
    object Dialog: NavDestinationType()
    object BottomSheet: NavDestinationType()
}