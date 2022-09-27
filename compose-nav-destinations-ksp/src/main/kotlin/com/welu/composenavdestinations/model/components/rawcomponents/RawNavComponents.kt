package com.welu.composenavdestinations.model.components.rawcomponents

import com.welu.composenavdestinations.model.components.ComposeNavGraphInfo
import com.welu.composenavdestinations.model.components.ComposeDestinationInfo

/**
 * Represents all raw Components that were annotate.
 * [ComposeNavGraphInfo] and [ComposeDestinationInfo] can then be generated with this information.
 */
data class RawNavComponents(
    val rawNavGraphInfos: Sequence<RawComposeNavGraphInfo>,
    val rawDestinationInfos: Sequence<RawComposeDestinationInfo>
)
