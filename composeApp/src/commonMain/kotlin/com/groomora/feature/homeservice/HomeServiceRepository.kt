package com.groomora.feature.homeservice

import kotlinx.coroutines.flow.Flow

interface HomeServiceRepository {
    fun getHomeServiceDetails(addressId: String): Flow<HomeServiceDetail?>
}
