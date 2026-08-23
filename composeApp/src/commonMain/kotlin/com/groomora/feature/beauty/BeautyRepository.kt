package com.groomora.feature.beauty

import kotlinx.coroutines.flow.Flow

interface BeautyRepository {
    fun getBeautyCategories(): Flow<List<BeautyCategory>>
    fun getBeautyServices(categoryId: String?): Flow<List<BeautyService>>
    fun getBeautyPackages(): Flow<List<BeautyPackage>>
}
