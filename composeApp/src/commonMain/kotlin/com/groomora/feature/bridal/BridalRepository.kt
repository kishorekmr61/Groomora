package com.groomora.feature.bridal

import kotlinx.coroutines.flow.Flow

interface BridalRepository {
    fun getBridalPackages(): Flow<List<BridalPackage>>
    suspend fun getPackageDetails(packageId: String): BridalPackage?
}
