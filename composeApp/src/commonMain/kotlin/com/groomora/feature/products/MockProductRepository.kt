package com.groomora.feature.products

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class MockProductRepository : ProductRepository {
    private val mockProducts = listOf(
        Product(
            id = "pr1",
            name = "Organic Argan Hair Growth Oil",
            brand = "Groomora Signature",
            price = 599.0,
            originalPrice = 799.0,
            imageUrl = "https://images.unsplash.com/photo-1608248597359-009eb73d6d03?w=500&q=80",
            description = "100% pure cold-pressed Moroccan argan oil for deep scalp therapy and shine.",
            category = "hair"
        ),
        Product(
            id = "pr2",
            name = "Matte Finish Styling Clay Wax",
            brand = "Style Master Pro",
            price = 450.0,
            originalPrice = 550.0,
            imageUrl = "https://images.unsplash.com/photo-1599351431202-1e0f0137899a?w=500&q=80",
            description = "Strong 24-hour hold with natural matte texture. Non-greasy and easy to wash off.",
            category = "hair"
        ),
        Product(
            id = "pr3",
            name = "Hydrating Vitamin C Face Wash",
            brand = "Ivory Skin Labs",
            price = 349.0,
            originalPrice = 449.0,
            imageUrl = "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&q=80",
            description = "Gentle foaming cleanser with brightening vitamin C and aloe vera extract.",
            category = "skin"
        ),
        Product(
            id = "pr4",
            name = "Beard Conditioning Growth Balm",
            brand = "The Barber Co.",
            price = 399.0,
            originalPrice = 499.0,
            imageUrl = "https://images.unsplash.com/photo-1621605815971-fbc98d665033?w=500&q=80",
            description = "Infused with cedarwood and jojoba oil to soften stiff beard and relieve itch.",
            category = "grooming"
        ),
        Product(
            id = "pr5",
            name = "Gold Radiance Facial Night Cream",
            brand = "Luxe Glow Therapy",
            price = 899.0,
            originalPrice = 1199.0,
            imageUrl = "https://images.unsplash.com/photo-1570172619644-dfd03ed5d881?w=500&q=80",
            description = "Overnight cellular renewal formula with 24k colloidal gold and niacinamide.",
            category = "skin"
        ),
        Product(
            id = "pr6",
            name = "Lavender & Chamomile Spa Mist",
            brand = "Aroma Botanicals",
            price = 499.0,
            originalPrice = 649.0,
            imageUrl = "https://images.unsplash.com/photo-1540555700478-4be289fbecef?w=500&q=80",
            description = "Calming aromatherapy mist for pillow, face hydration, and relaxing bedtime spa.",
            category = "spa"
        ),
        Product(
            id = "pr7",
            name = "Keratin Repair Hair Mask",
            brand = "Salon Selectives",
            price = 749.0,
            originalPrice = 999.0,
            imageUrl = "https://images.unsplash.com/photo-1527799820374-dcf8d9d4a388?w=500&q=80",
            description = "Intense deep conditioning treatment for color-treated and heat-damaged hair.",
            category = "hair"
        ),
        Product(
            id = "pr8",
            name = "Activated Charcoal Detan Scrub",
            brand = "Pure Detox",
            price = 379.0,
            originalPrice = 499.0,
            imageUrl = "https://images.unsplash.com/photo-1512290900672-1f02e71d4793?w=500&q=80",
            description = "Exfoliates dead skin cells, unclogs stubborn pores, and reverses tan naturally.",
            category = "skin"
        ),
        Product(
            id = "pr9",
            name = "Sandalwood Post-Shave Soothing Balm",
            brand = "Royal Grooming",
            price = 429.0,
            originalPrice = 549.0,
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=500&q=80",
            description = "Zero-alcohol cooling lotion that calms razor burn and locks in skin moisture.",
            category = "grooming"
        ),
        Product(
            id = "pr10",
            name = "Ultra UV Sunscreen Gel SPF 50",
            brand = "Sun Shield Dermatology",
            price = 549.0,
            originalPrice = 699.0,
            imageUrl = "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?w=500&q=80",
            description = "Broad spectrum PA++++ protection with matte non-sticky finish and no white cast.",
            category = "skin"
        ),
        Product(
            id = "pr11",
            name = "Nourishing Cuticle & Nail Elixir",
            brand = "Nail Couture",
            price = 299.0,
            originalPrice = 399.0,
            imageUrl = "https://images.unsplash.com/photo-1632345031435-8727f6897d53?w=500&q=80",
            description = "Strengthens brittle nails and hydrates dry cuticles with sweet almond oil.",
            category = "spa"
        ),
        Product(
            id = "pr12",
            name = "Volumizing Sea Salt Texture Spray",
            brand = "Style Master Pro",
            price = 479.0,
            originalPrice = 599.0,
            imageUrl = "https://images.unsplash.com/photo-1519699047748-de8e457a634e?w=500&q=80",
            description = "Adds effortless beach wave volume and natural movement with light flexible hold.",
            category = "hair"
        )
    )

    private val _cartItems = MutableStateFlow<Map<String, Int>>(emptyMap())
    override val cartItems: StateFlow<Map<String, Int>> = _cartItems.asStateFlow()

    override fun addToCart(productId: String) {
        val current = _cartItems.value.toMutableMap()
        val count = current[productId] ?: 0
        current[productId] = count + 1
        _cartItems.value = current
    }

    override fun removeFromCart(productId: String) {
        val current = _cartItems.value.toMutableMap()
        val count = current[productId] ?: 0
        if (count > 1) {
            current[productId] = count - 1
        } else {
            current.remove(productId)
        }
        _cartItems.value = current
    }

    override fun clearCart() {
        _cartItems.value = emptyMap()
    }

    override fun getProducts(): Flow<List<Product>> = flow {
        delay(300)
        emit(mockProducts)
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> = flow {
        delay(200)
        emit(mockProducts.filter { it.category.equals(category, ignoreCase = true) })
    }

    override fun searchProducts(query: String): Flow<List<Product>> = flow {
        delay(200)
        emit(mockProducts.filter { it.name.contains(query, ignoreCase = true) || it.brand.contains(query, ignoreCase = true) })
    }

    override suspend fun getProductDetails(productId: String): Product? {
        delay(200)
        return mockProducts.find { it.id == productId }
    }
}

