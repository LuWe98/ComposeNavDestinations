package com.welu.composenavdestinations.model.structure

import com.welu.composenavdestinations.model.NavGraphInfo
import com.welu.composenavdestinations.model.NavDestinationInfo

/**
 * Represents all raw Components of the App. [NavGraphInfo] and [NavDestinationInfo] can then be generated with this information.
 */
data class RawNavComponents(
    val rawNavGraphInfos: Sequence<RawNavGraphInfo>,
    val rawNavDestinationInfos: Sequence<RawNavDestinationInfo>
)
