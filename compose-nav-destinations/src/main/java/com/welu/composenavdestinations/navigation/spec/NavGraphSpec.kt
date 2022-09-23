package com.welu.composenavdestinations.navigation.spec

//TODO -> Eigenschafften von so nem NavGraph hinschreiben, so wie bei Destinations
// -> Ein NavGraph beitzt referenzen auf die zugehörigen DestinationSpecs und auf den ParentNavGraphen.
// -> Extension functions implementieren, um auch euf einen bestimmten NavGraph zu navigieren. Ein NavGraph muss deshalb Routable sein.
// -> Wenn die erste Destination von einem NavGraph Parameter besitzt, dann muss Routable zu dem NavGraph auch diese Parameter bei Invoke übergeben werden
// -> Wieder PlainNavGraphspec und ArgNavGraphSpec einbauen
// -> Schauen wegen den NavArgs -> Möglicherweise wieder PlainNavGraphSpec und ArgNavGraphSpec erstellen. Die Args dann über die Annotation als Class übergeben.
//    Ist aber irgendwie flawed weil man auch zu ner Destination innerhalb des Graphen navigieren kann ohne Argumente für den Graphen zu übergeben.
//    Das selbe gilt bei NavGraphs. Wenn man zu nem NavGraph navigiert, wird man zwangsläufig mit dem startDestination Parameter auf einer Destination umgeleitet.
//    Dadurch kann es passieren, dass man an den NavGraph zwar Argumente übergibt, aber keine Argumente für das Composable hat.
//    --> Deshalb vllt dem NavGraph die Argumente von der StartDestination übergeben?

// TODO -> Standardmäßig alle Destinations zu einem Default NavGraph hinzufügen -> Dieser kann dann verwendet werden um einen NavHost() zu erstellen / zu bekommen (Composable)


//TODO -> noch als Sealed Klasse umbauen
/**
 * Defines a NavGraph with associated Destinations
 */
sealed interface NavGraphSpec: NavComponentSpec {

    /**
     * This is the start of the NavGraph. Can either be a [DestinationSpec] or [NavGraphSpec].
     * It is not nullable since a startDestination has to be defined in order to navigate to a [NavGraphSpec].
     *
     * Example:
     *
     *     navigation(startDestination = "nav2", route = "nav1") {
     *          navigation(startDestination = "exampleDestination", route = "nav2") {
     *              composable(route = "exampleDestination") { }
     *          }
     *     }
     */
    val startComponentSpec: NavComponentSpec

    /**
     * Returns if this [NavGraphSpec] is a root
     */
    val isRoot: Boolean get() = parentNavGraphSpec == null

    /**
     * This list contains all nested [NavComponentSpec]s inside this [NavGraphSpec]
     */
    val childNavComponentSpecs: List<NavComponentSpec>

    /**
     * This list contains all nested [NavGraphSpec]s inside this [NavGraphSpec].
     */
    val childNavGraphSpecs: List<NavGraphSpec> get() = childNavComponentSpecs.filterIsInstance<NavGraphSpec>()

    /**
     * This list contains all [DestinationSpec]s inside this [NavGraphSpec].
     */
    val childDestinationSpecs: List<DestinationSpec<*>> get() = childNavComponentSpecs.filterIsInstance<DestinationSpec<*>>()

}