package com.groomora.feature.discovery

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockDiscoveryRepository : DiscoveryRepository {
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
        ),
        Shop(
            id = "s2",
            name = "Ivory Beauty Lab",
            rating = 4.9,
            reviewCount = 89,
            address = "12 Uptown Street",
            distance = "2.5 km",
            imageUrl = "",
            isVerified = true,
            categories = listOf("makeup", "nails", "skin"),
            tags = listOf("Luxury", "Women")
        ),
        Shop(
            id = "s3",
            name = "Home Groomers",
            rating = 4.7,
            reviewCount = 210,
            address = "Service Area: City Central",
            distance = "Mobile",
            imageUrl = "",
            isVerified = true,
            categories = listOf("hair", "beard", "home"),
            tags = listOf("Home Service", "Unisex")
        )
    )

    override fun getShops(): Flow<List<Shop>> = flow {
        delay(800)
        emit(mockShops)
    }

    override fun getShopsByCategory(categoryId: String): Flow<List<Shop>> = flow {
        delay(500)
        emit(mockShops.filter { it.categories.contains(categoryId) })
    }

    override fun searchShops(query: String): Flow<List<Shop>> = flow {
        delay(300)
        emit(mockShops.filter { it.name.contains(query, ignoreCase = true) })
    }

    override suspend fun getShopDetails(shopId: String): Shop? {
        delay(300)
        return mockShops.find { it.id == shopId }
    }

    override suspend fun getProfessionals(shopId: String): List<Professional> {
        delay(500)
        return listOf(
            Professional("p1", "Alex Rivera", "Senior Stylist", 4.9, ""),
            Professional("p2", "Sarah Chen", "Color Specialist", 4.8, "")
        )
    }

    override suspend fun getProfessionalDetail(id: String): ProfessionalDetail? {
        delay(600)
        return ProfessionalDetail(
            id = id,
            name = if (id == "p1") "Alex Rivera" else "Sarah Chen",
            role = if (id == "p1") "Senior Stylist" else "Color Specialist",
            rating = 4.9,
            reviewCount = 156,
            imageUrl = "",
            bio = "Master of contemporary cuts and traditional shaves with over 10 years of international experience.",
            skills = listOf("Precision Cutting", "Hot Towel Shave", "Beard Sculpting", "Hair Tattooing"),
            portfolioImages = listOf("", "", "", ""),
            yearsOfExperience = 12,
            isVerified = true
        )
    }
}
