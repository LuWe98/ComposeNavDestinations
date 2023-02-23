package com.welu.compose_nav_destinations_ksp.extensions.ksp

import com.google.devtools.ksp.symbol.FileLocation
import com.google.devtools.ksp.symbol.Location
import com.welu.compose_nav_destinations_ksp.extensions.file
import com.welu.compose_nav_destinations_ksp.extensions.readLine

// Location
val Location.fileLine get() = (this as FileLocation).file.readLine(lineNumber)
