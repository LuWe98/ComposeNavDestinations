package com.welu.composenavdestinations.navigation.scope

import androidx.compose.foundation.layout.ColumnScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.BottomSheetArgDestinationSpec

data class BottomSheetArgDestinationScope<Arg: Any>(
    override val relatedSpec: BottomSheetArgDestinationSpec<Arg>,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry,
    val columnScope: ColumnScope
): ComposeArgDestinationScope<Arg> {

    override val args: Arg by lazy(LazyThreadSafetyMode.NONE) {
        relatedSpec.argsFrom(backStackEntry)
    }

}