package com.groomora.feature.products

import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CartAndOrdersTest {

    @Test
    fun testCartStatePersistenceAcrossRepository() {
        val repo = MockProductRepository()

        // Initially cart is empty
        assertEquals(0, repo.cartItems.value.size)

        // Add 2 items of pr1 and 1 item of pr2
        repo.addToCart("pr1")
        repo.addToCart("pr1")
        repo.addToCart("pr2")

        assertEquals(2, repo.cartItems.value["pr1"])
        assertEquals(1, repo.cartItems.value["pr2"])

        // Decrease quantity of pr1
        repo.removeFromCart("pr1")
        assertEquals(1, repo.cartItems.value["pr1"])

        // Remove pr2
        repo.removeFromCart("pr2")
        assertFalse(repo.cartItems.value.containsKey("pr2"))

        // Clear cart
        repo.clearCart()
        assertTrue(repo.cartItems.value.isEmpty())
    }

    @Test
    fun testCartPricingCalculations() {
        val p1 = Product("pr1", "Hair Oil", "Brand A", 599.0, null, "", "", "hair")
        val p2 = Product("pr2", "Wax", "Brand B", 450.0, null, "", "", "hair")

        val cart = mapOf(p1.id to 1, p2.id to 1) // 599 + 450 = 1049 ( > 999 -> Free delivery )
        val subtotal = p1.price * cart[p1.id]!! + p2.price * cart[p2.id]!!
        val deliveryFee = if (subtotal > 999.0) 0.0 else 49.0
        val total = subtotal + deliveryFee

        assertEquals(1049.0, subtotal)
        assertEquals(0.0, deliveryFee)
        assertEquals(1049.0, total)
    }

    @Test
    fun testOrderPlacementAndTrackingVisibilityRules() = runBlocking {
        val orderRepo = MockOrderRepository()
        val items = listOf(OrderItem("pr1", "Hair Oil", 1, 599.0))

        val order = orderRepo.placeOrder(
            items = items,
            totalAmount = 599.0,
            address = "221B Baker Street",
            paymentMethod = "UPI"
        )

        assertNotNull(order)
        assertTrue(order.id.startsWith("ORD-"))
        assertEquals(599.0, order.totalAmount)
        assertEquals(OrderStatus.PLACED, order.status)

        // Verification: Newly placed order has no courier number initially
        assertNull(order.trackingNumber)
        // Verification: No cancellation allowed
        assertFalse(order.canCancel)
    }
}
