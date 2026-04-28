package com.choiminjun.navigation

import kotlinx.serialization.Serializable

sealed interface Route


@Serializable
data object HomeBaseRoute : Route

sealed interface HomeGraph : Route {
    @Serializable
    data object HomeRoute : HomeGraph
}

