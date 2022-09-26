package com.welu.composenavdestinations.navigation.scope

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.DialogDestinationSpec

data class DialogDestinationScope(
    override val relatedSpec: DialogDestinationSpec,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry
): ComposeDestinationScope