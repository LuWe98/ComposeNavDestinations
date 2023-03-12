package com.welu.composenavdestinations.service

internal object ServiceLocator {

    val composeDestinationService by lazy {
        ComposeDestinationService()
    }
}