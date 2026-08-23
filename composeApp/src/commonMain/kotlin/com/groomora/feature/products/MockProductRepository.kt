package com.groomora.feature.products

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockProductRepository : ProductRepository {
    private val mockProducts = listOf(
        Product(
            id = "pr1",
            name = "Premium Beard Oil",
            brand = "Groomora Signature",
            price = 850.0,
            originalPrice = 999.0,
            imageUrl = "",
            description = "Natural oils to keep your beard soft and healthy.",
            category = "grooming"
        ),
        Product(
            id = "pr2",
            name = "Matte Clay Wax",
            brand = "Style Master",
            price = 450.0,
            imageUrl = "",
            description = "Strong hold with a natural matte finish.",
            category = "hair"
        ),
        Product(
            id = "pr3",
            name = "Hydrating Face Wash",
            brand = "Ivory Skin",
            price = 350.0,
            imageUrl = "",
            description = "Gentle cleansing for all skin types.",
            category = "skin"
        )
    )

    override fun getProducts(): Flow<List<Product>> = flow {
        delay(600)
        emit(mockProducts)
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        delay(400)
        emit(mockProducts.filter { it.category == category })
    }

    override fun searchProducts(query: String): Flow<List<Product>> = flow {
        delay(300)
        emit(mockProducts.filter { it.name.contains(query, ignoreCase = true) })
    }

    override suspend fun getProductDetails(productId: String): Product? {
        delay(300)
        return mockProducts.find { it.id == productId }
    }
}
