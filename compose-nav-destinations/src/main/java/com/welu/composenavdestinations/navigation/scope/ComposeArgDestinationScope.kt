package com.welu.composenavdestinations.navigation.scope

import com.welu.composenavdestinations.navigation.spec.ComposeArgDestinationSpec

sealed interface ComposeArgDestinationScope<Arg: Any>: ComposeDestinationScope {

    override val relatedSpec: ComposeArgDestinationSpec<Arg, *>

    val args: Arg

}