package com.welu.composenavdestinations.validation

import com.welu.composenavdestinations.exceptions.DuplicateDestinationException
import com.welu.composenavdestinations.exceptions.DuplicateRouteException
import com.welu.composenavdestinations.model.components.ComposeDestinationInfo

//TODO -> Validation Classes bauen -> NavDestinationValidator, NavGraphValidator, NavComponentValidator
object NavDestinationValidator {

    fun validate(destinations: Sequence<ComposeDestinationInfo>) {
        val areDuplicateNamesPresent = destinations.groupBy(ComposeDestinationInfo::simpleName).any { it.value.size > 1 }
        if(areDuplicateNamesPresent) {
            throw DuplicateDestinationException
        }

        val areDuplicateRoutesPresent = destinations.groupBy(ComposeDestinationInfo::baseRoute).any { it.value.size > 1 }
        if(areDuplicateRoutesPresent) {
            throw DuplicateRouteException
        }

    }

}