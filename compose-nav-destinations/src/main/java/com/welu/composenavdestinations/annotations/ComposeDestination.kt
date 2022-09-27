package com.welu.composenavdestinations.annotations

@Target(AnnotationTarget.CLASS)
annotation class ComposeDestination(
    val route: String = "",
    val deepLinks: Array<NavComponentDeepLink> = []
)

//TODO -> Sagt KSP, dass eine eigene NavArgs Klasse in dem Spec erstellt werden soll
// -> Schwierig wegen Namen
// -> Also das auch hier festlegen i guess
// -> Name wird aber eh Args sein -> Also wenn es ArgsDestination<String> wär dann würde es args in dem Scope heißen.
//val useNavArgsAsWholeObject: Boolean = false,

/*
    //TODO -> Das noch einbauen - OLD: val type: KClass<out NavDestinationType> = NavDestinationType.Default::class,
    val destinationType: KClass<*> = Unit::class,
 */