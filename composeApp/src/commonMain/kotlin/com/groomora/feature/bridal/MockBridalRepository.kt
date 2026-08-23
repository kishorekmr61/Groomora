package com.groomora.feature.bridal

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockBridalRepository : BridalRepository {
    private val mockPackages = listOf(
        BridalPackage(
            id = "br1",
            name = "Royal Bridal Radiance",
            description = "Complete bridal makeover including HD makeup, luxury hairstyling, and saree/lehenga draping.",
            price = 15000.0,
            items = listOf("HD Makeup", "Luxury Hairstyling", "Saree Draping", "Skin Prep"),
            isHomeServiceAvailable = true
        ),
        BridalPackage(
            id = "br2",
            name = "Engagement Glow",
            description = "Sophisticated look for your engagement ceremony. Includes makeup and hairstyling.",
            price = 8000.0,
            items = listOf("Engagement Makeup", "Hairstyling", "Draping"),
            isHomeServiceAvailable = true
        ),
        BridalPackage(
            id = "br3",
            name = "Bridal Party Package",
            description = "Package for bridesmaids and family. Minimalist yet elegant makeup and hair.",
            price = 3500.0,
            items = listOf("Party Makeup", "Simple Hairstyling"),
            isHomeServiceAvailable = true
        )
    )

    override fun getBridalPackages(): Flow<List<BridalPackage>> = flow {
        delay(800)
        emit(mockPackages)
    }

    override suspend fun getPackageDetails(packageId: String): BridalPackage? {
        delay(400)
        return mockPackages.find { it.id == packageId }
    }
}
