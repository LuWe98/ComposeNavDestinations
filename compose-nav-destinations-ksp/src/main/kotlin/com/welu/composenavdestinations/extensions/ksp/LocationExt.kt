package com.welu.composenavdestinations.extensions.ksp

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.Location
import com.welu.composenavdestinations.extensions.file
import com.welu.composenavdestinations.extensions.readLine

// Location
val Location.fileLine get() = (this as FileLocation).file.readLine(lineNumber)
