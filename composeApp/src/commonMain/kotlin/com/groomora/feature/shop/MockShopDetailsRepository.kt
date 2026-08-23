package com.groomora.feature.shop

import com.groomora.feature.discovery.Professional
import com.groomora.feature.discovery.Shop
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockShopDetailsRepository : ShopDetailsRepository {
    private val mockShops = listOf(
        Shop(
            id = "s1",
            name = "The Golden Scissor",
            rating = 4.8,
            reviewCount = 124,
            address = "45 Downtown Ave",
            distance = "1.2 km",
            imageUrl = "",
            isVerified = true,
            categories = listOf("hair", "beard"),
            tags = listOf("Premium", "Men")
        )
    )

    private val mockServices = listOf(
        Service("ser1", "Classic Haircut", 450.0, "45 min", "A precision cut tailored to your style.", "hair"),
        Service("ser2", "Beard Trim & Shape", 250.0, "30 min", "Professional grooming for your beard.", "beard"),
        Service("ser3", "Head Massage", 300.0, "20 min", "Relaxing scalp massage with essential oils.", "hair")
    )

    private val mockPackages = listOf(
        ServicePackage(
            id = "pkg1",
            name = "The Royal Groom",
            price = 600.0,
            originalPrice = 700.0,
            services = listOf("ser1", "ser2"),
            description = "Haircut + Beard Trim bundle."
        )
    )

    override fun getShop(shopId: String): Flow<Shop?> = flow {
        delay(300)
        emit(mockShops.find { it.id == shopId })
    }

    override fun getServices(shopId: String): Flow<List<Service>> = flow {
        delay(400)
        emit(mockServices)
    }

    override fun getPackages(shopId: String): Flow<List<ServicePackage>> = flow {
        delay(400)
        emit(mockPackages)
    }

    override fun getProfessionals(shopId: String): Flow<List<Professional>> = flow {
        delay(500)
        emit(listOf(
            Professional("p1", "Alex Rivera", "Senior Stylist", 4.9, ""),
            Professional("p2", "Sarah Chen", "Color Specialist", 4.8, "")
        ))
    }
}
