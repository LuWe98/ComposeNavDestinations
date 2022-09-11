package com.welu.composenavdestinations.navgraphs

//TODO -> Eigenschafften von so nem NavGraph hinschreiben, so wie bei Destinations
// -> Ein NavGraph beitzt referenzen auf die zugehörigen DestinationSpecs und auf den ParentNavGraphen.
// -> Extension functions implementieren, um auch euf einen bestimmten NavGraph zu navigieren. Ein NavGraph muss deshalb Routable sein.
// -> Wenn die erste Destination von einem NavGraph Parameter besitzt, dann muss Routable zu dem NavGraph auch diese Parameter bei Invoke übergeben werden
sealed interface NavGraph