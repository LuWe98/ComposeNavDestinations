package com.welu.composenavdestinations.navigation.scope

import androidx.compose.foundation.layout.ColumnScope
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.welu.composenavdestinations.navigation.spec.BottomSheetDestinationSpec

data class BottomSheetDestinationScope(
    override val relatedSpec: BottomSheetDestinationSpec,
    override val navController: NavHostController,
    override val backStackEntry: NavBackStackEntry,
    val columnScope: ColumnScope
): ComposeDestinationScope